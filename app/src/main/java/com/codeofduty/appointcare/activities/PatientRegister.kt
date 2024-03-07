package com.codeofduty.appointcare.activities

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.databinding.ActivityPatientRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import patientRegister
import retrofit2.Call
import retrofit2.Callback
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException

class PatientRegister : AppCompatActivity() {
    private lateinit var binding: ActivityPatientRegisterBinding
    private var selectedImageUri: Uri? = null
    private val disposables = CompositeDisposable()
    private var imageFile: File? = null // Declare imageFile here


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //First Name Validation//

        val FNameStream = RxTextView.textChanges(binding.FNameRegEditText)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        FNameStream.subscribe {
            showFNameExistAlert(it)
        }
        //Last Name Validation//

        val LNameStream = RxTextView.textChanges(binding.LNameRegEditText)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        LNameStream.subscribe {
            showLNameExistAlert(it)
        }

        //Phone Number Validation

        val phoneNumberStream = RxTextView.textChanges(binding.PhoneNumRegEditText)
            .skipInitialValue()
            .map { phoneNumber ->
                phoneNumber.length >= 12
            }
        phoneNumberStream.subscribe {
            showPhoneNumExistAlert(it)
        }

        //GENDER VALIDATION

        val genderStream = RxTextView.textChanges(binding.genderEditText)
            .skipInitialValue()
            .map { gender ->
                gender.toString() !in listOf("Male", "Female")
            }
        genderStream.subscribe {
            showgenderExistAlert(it)
        }


        //EMAIL VALIDATION

        val EMAILStream = RxTextView.textChanges(binding.emailRegEditText)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        EMAILStream.subscribe {
            showEMAILExistAlert(it)
        }
        //AGE Validation//

        val AgeStream = RxTextView.textChanges(binding.AgeRegEditText)
            .skipInitialValue()
            .map { age ->
                age.length > 2
            }
        AgeStream.subscribe {
            showAgeExistAlert(it)
        }

        //PASSWORD VALIDATION
        val passwordStream = RxTextView.textChanges(binding.PasswordRegEditText)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showPasswordAlert(it)
        }
        // CONFIRM PASSWORD VALIDATION
        val confirmPasswordStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.PasswordRegEditText)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.ConfirmPasswordRegEditText.text.toString()
                },
            RxTextView.textChanges(binding.ConfirmPasswordRegEditText)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.PasswordRegEditText.text.toString()
                })
        confirmPasswordStream.subscribe {
            showConfirmPasswordAlert(it)
        }

        //ENABLE BUTTON TRUE OR FALSE

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            FNameStream,
            LNameStream,
            phoneNumberStream,
            EMAILStream,
            AgeStream,
            passwordStream,
            confirmPasswordStream,
            { FnameInvalid: Boolean, LnameInvalid: Boolean, phonenumInvalid: Boolean, emailInvalid: Boolean, ageInvalid: Boolean, passwordInvalid: Boolean, confirmPassInvalid: Boolean ->
                !FnameInvalid && !LnameInvalid && !phonenumInvalid && !emailInvalid && !ageInvalid && !passwordInvalid && !confirmPassInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(
                    this,
                    R.color.primarycolor
                )
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }


// CLICK
        binding.btnRegister.setOnClickListener {
            Toast.makeText(this@PatientRegister, "Please wait...", Toast.LENGTH_SHORT).show()

            registerPatient()
        }
        binding.haveAccBTN.setOnClickListener {
            startActivity(Intent(this, UserLogin::class.java))
        }

        binding.btnUploadPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    selectedImageUri = uri
                    binding.imageView.setImageURI(uri)
                    binding.imageView.visibility = View.VISIBLE
                } catch (e: FileNotFoundException) {
                    Toast.makeText(this, "Selected image not found", Toast.LENGTH_SHORT).show()
                    selectedImageUri = null
                }
            } ?: run {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
                selectedImageUri = null
            }
        }
    }



    private fun registerPatient() {

        // Show loading dialog
        val loadingDialog = createLoadingDialog()
        loadingDialog.show()


        val FName = binding.FNameRegEditText.text.toString().trim()
        val LName = binding.LNameRegEditText.text.toString().trim()
        val phoneNumber = binding.PhoneNumRegEditText.text.toString().trim()
        val gender = binding.genderEditText.text.toString().trim()
        val email = binding.emailRegEditText.text.toString().trim()
        val age = binding.AgeRegEditText.text.toString().trim().toInt()
        val password = binding.PasswordRegEditText.text.toString().trim()

        selectedImageUri?.let { uri ->
            // Get the file path from the URI
            val imagePath = uri.getPathFromURI(this)
            imagePath?.let { path ->
                // Create a file object from the file path
                val imageFile = File(path)
                // Display the image
                binding.imageView.setImageURI(uri)
                binding.imageView.visibility = View.VISIBLE

                // Check if imageFile is null before making the API call
                imageFile?.let { file ->
                    val mediaType = "image/png"
                    val fileName = "photo_${System.currentTimeMillis()}.png"
                    val reqFile = file.asRequestBody(mediaType.toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", fileName, reqFile)

                    // Make the API call
                    val call = RetrofitClient.getService().registerPatient(
                        role = RequestBody.create("text/plain".toMediaTypeOrNull(), "Patient"),
                        fName = RequestBody.create("text/plain".toMediaTypeOrNull(), FName),
                        lName = RequestBody.create("text/plain".toMediaTypeOrNull(), LName),
                        phoneNumber = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber),
                        gender = RequestBody.create("text/plain".toMediaTypeOrNull(), gender),
                        age = RequestBody.create("text/plain".toMediaTypeOrNull(), age.toString()),
                        email = RequestBody.create("text/plain".toMediaTypeOrNull(), email),
                        password = RequestBody.create("text/plain".toMediaTypeOrNull(), password),
                        image = imagePart
                    )

                    call.enqueue(object : Callback<patientRegister> {
                        override fun onResponse(call: Call<patientRegister>, response: Response<patientRegister>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@PatientRegister, "Patient registered successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@PatientRegister, UserLogin::class.java))
                                finish()
                            } else {
                                val errorMessage = "Registration failed: ${response.errorBody()?.string()}"
                                Toast.makeText(this@PatientRegister, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<patientRegister>, t: Throwable) {
                            Toast.makeText(this@PatientRegister, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@PatientRegister, UserLogin::class.java))
                            finish()
                        }
                    })
                }
            }
        }
    }

    private fun createLoadingDialog(): AlertDialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Prevent dialog dismissal on outside touch or back press

        val alertDialog = builder.create()

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_loading_background)


        return alertDialog


    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
    private fun Uri.getPathFromURI(context: Context): String? {
        var realPath: String? = null
        val uriScheme = this.scheme
        if (uriScheme == null)
            realPath = this.path
        else if (ContentResolver.SCHEME_CONTENT == uriScheme) {
            val cursor: Cursor? = context.contentResolver.query(this, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    realPath = cursor.getString(column_index)
                }
                cursor.close()
            }
        } else if (uriScheme == ContentResolver.SCHEME_FILE) {
            realPath = this.path
        }
        return realPath
    }




// SHOW ALERTS!

    // FNAME ALERT
    private fun showFNameExistAlert(isNotValid: Boolean) {
        binding.FNameRegEditText.error = if (isNotValid) "Invalid First Name" else null
    }

    // LNAME ALERT
    private fun showLNameExistAlert(isNotValid: Boolean) {
        binding.LNameRegEditText.error = if (isNotValid) "Invalid Last Name" else null
    }

    // NUMBER ALERT
    private fun showPhoneNumExistAlert(isNotValid: Boolean) {
        binding.PhoneNumRegEditText.error = if (isNotValid) "Cannot exceed 11 characters" else null
    }

    // GENDER ALERT
    private fun showgenderExistAlert(isNotValid: Boolean) {
        binding.genderEditText.error = if (isNotValid) "Invalid Gender (Male or Female)" else null
    }

    // EMAIL ALERT
    private fun showEMAILExistAlert(isNotValid: Boolean) {
        binding.emailRegEditText.error = if (isNotValid) "Invalid eMAIL" else null
    }

    // AGE ALERT
    private fun showAgeExistAlert(isNotValid: Boolean) {
        binding.AgeRegEditText.error = if (isNotValid) "Invalid Age" else null
    }

    // PASSWORD ALERT
    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.PasswordRegEditText.error = if (isNotValid) {
            binding.PasswordRegLayout.endIconMode = TextInputLayout.END_ICON_NONE
            "Password should be minimum of 8 characters"
        } else {
            binding.PasswordRegLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            null
        }
    }

    // CONFIRM PASSWORD ALERT
    private fun showConfirmPasswordAlert(isNotValid: Boolean) {
        binding.ConfirmPasswordRegEditText.error = if (isNotValid) {
            binding.ConfirmPasswordRegLayout.endIconMode = TextInputLayout.END_ICON_NONE
            "Password does not match"
        } else {
            binding.ConfirmPasswordRegLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            null
        }
    }
}