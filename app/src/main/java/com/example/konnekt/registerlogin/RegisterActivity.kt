package com.example.konnekt.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.konnekt.R
import com.example.konnekt.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    companion object {
        val TAG = "RegisterActivity"
    }

    private val firebase: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        selectphoto.setOnClickListener {
            val selectphotointent = Intent(Intent.ACTION_PICK)
            selectphotointent.type = "image/*"

            startActivityForResult(selectphotointent, 0)
        }
        registrationButton.setOnClickListener {
            createAccount()
        }
    }

    var selectedphoto: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedphoto = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedphoto)

            selectphotocircleView.setImageBitmap(bitmap)

            selectphoto.alpha = 0f
        }
    }

    private fun createAccount() {
        val email = signupemailPrompt.text.toString()
        val firstName = signupfirstName.text.toString()
        val lastName = signuplastName.text.toString()
        val usernamePut = username.text.toString()
        val password = signuppasswordPrompt.text.toString()
        val passwordCheck = signuppasswordConfirm.text.toString()

        //validate input details
        if (firstName.isEmpty() || password.isEmpty() || lastName.isEmpty()
            || email.isEmpty() || passwordCheck.isEmpty() || usernamePut.isEmpty()
        ) {
            Toast.makeText(this, "Missing fields required", Toast.LENGTH_SHORT).show()
            return
        } else if (password.length < 6) {
            Toast.makeText(this, "Your password must have at least 6 characters.", Toast.LENGTH_SHORT).show()
            return
        } else if (password != passwordCheck) {
            Toast.makeText(this, "Passwords are not the same.", Toast.LENGTH_SHORT).show()
            return
        } else {

            firebase.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Toast.makeText(this, "Successfully created user", Toast.LENGTH_SHORT).show()
                    saveusertodatase()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun saveusertodatase() {
        savephototodatabase()
    }

    private fun savephototodatabase() {
        if (selectedphoto == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedphoto!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveinfotodatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun saveinfotodatabase(profileimageurl: String) {
        val uid = firebase.uid ?: ""
        val reference = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            signupfirstName.text.toString(),
            signuplastName.text.toString(),
            username.text.toString(),
            profileimageurl
        )

        reference.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully saved user to database", Toast.LENGTH_SHORT).show()
                val profileintent = Intent(this, ProfileActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(profileintent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Fail: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


}





