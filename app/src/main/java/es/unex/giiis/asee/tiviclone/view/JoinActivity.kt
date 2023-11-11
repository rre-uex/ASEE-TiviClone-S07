package es.unex.giiis.asee.tiviclone.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import es.unex.giiis.asee.tiviclone.databinding.ActivityJoinBinding
import es.unex.giiis.asee.tiviclone.data.model.User
import es.unex.giiis.asee.tiviclone.database.TiviCloneDatabase
import es.unex.giiis.asee.tiviclone.util.CredentialCheck
import kotlinx.coroutines.launch

class JoinActivity : AppCompatActivity() {

    private lateinit var db: TiviCloneDatabase

    private lateinit var binding: ActivityJoinBinding

    companion object {

        const val USERNAME = "JOIN_USERNAME"
        const val PASS = "JOIN_PASS"
        fun start(
            context: Context,
            responseLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, JoinActivity::class.java)
            responseLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TiviCloneDatabase.getInstance(applicationContext)!!

        //views initialization and listeners
        setUpUI()
        setUpListeners()
    }

    private fun setUpUI() {
        //get attributes from xml using binding
    }

    private fun setUpListeners() {
        with(binding) {
            btRegister.setOnClickListener {
                join()
            }
        }
    }

    private fun join() {
        with(binding) {
            val check = CredentialCheck.join(
                etUsername.text.toString(),
                etPassword.text.toString(),
                etRepassword.text.toString()
            )
            if (check.fail) notifyInvalidCredentials(check.msg)
            else {
                lifecycleScope.launch{
                    val user = User(
                        null,
                        etUsername.text.toString(),
                        etPassword.text.toString()
                    )
                    val id =  db?.userDao()?.insert(user)

                    navigateBackWithResult(
                        User(
                            id,
                            etUsername.text.toString(),
                            etPassword.text.toString()
                        )
                    )
                }

            }
        }
    }

    private fun navigateBackWithResult(user: User) {
        val intent = Intent().apply {
            putExtra(USERNAME,user.name)
            putExtra(PASS,user.password)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}