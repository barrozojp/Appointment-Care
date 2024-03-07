package com.codeofduty.appointcare.activities

import User
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
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
import com.codeofduty.appointcare.databinding.ActivityDoctorRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException

class DoctorRegister : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorRegisterBinding
    private var f2fChecked = false
    private var onlineChecked = false
    private var selectedImageUri: Uri? = null
    private val disposables = CompositeDisposable()
    private var imageFile: File? = null // Declare imageFile here


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorRegisterBinding.inflate(layoutInflater)
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
                phoneNumber.length >= 11
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

        //SPECIALITY
        val specialityStream = RxTextView.textChanges(binding.specialityEditText)
            .skipInitialValue()
            .map { speciality ->
                speciality.isEmpty()
            }
        specialityStream.subscribe {
            showSpecialityExistAlert(it)
        }

        //MD YEAR
        val mdStream = RxTextView.textChanges(binding.mdEditText)
            .skipInitialValue()
            .map { md ->
                md.length == 5
            }
        mdStream.subscribe {
            showmdExistAlert(it)
        }
        //CONSTULTATION PRICE
        val cnsltStream = RxTextView.textChanges(binding.cnsltPriceEditText)
            .skipInitialValue()
            .map { cnslt ->
                cnslt.isEmpty()
            }
        cnsltStream.subscribe {
            cnsltExistAlert(it)
        }
        //PROVINCE
        val ProvinceStream = RxTextView.textChanges(binding.provinceEditText)
            .skipInitialValue()
            .map { province ->
                province.isEmpty()
            }
        ProvinceStream.subscribe {
            provinceExistAlert(it)
        }
        //MUNICIPALITY
        val municipalityStream = RxTextView.textChanges(binding.municipalityEditText)
            .skipInitialValue()
            .map { municipality ->
                municipality.isEmpty()
            }
        municipalityStream.subscribe {
            municipalityExistAlert(it)
        }
        //BARANGGAY
        val brngyStream = RxTextView.textChanges(binding.municipalityEditText)
            .skipInitialValue()
            .map { brngy ->
                brngy.isEmpty()
            }
        brngyStream.subscribe {
            brngyExistAlert(it)
        }

        //HOUSE NUMBER
        val hnStream = RxTextView.textChanges(binding.hnEditText)
            .skipInitialValue()
            .map { hn ->
                hn.isEmpty()
            }
        hnStream.subscribe {
            hnExistAlert(it)
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
            specialityStream,
            mdStream,
            passwordStream,
            confirmPasswordStream,
            { FnameInvalid: Boolean, LnameInvalid: Boolean, phonenumInvalid: Boolean, emailInvalid: Boolean, ageInvalid: Boolean, specialityInvalid: Boolean, mdInvalid: Boolean,  passwordInvalid: Boolean, confirmPassInvalid: Boolean ->
                !FnameInvalid && !LnameInvalid && !phonenumInvalid && !emailInvalid && !ageInvalid && !specialityInvalid && !mdInvalid &&  !passwordInvalid && !confirmPassInvalid
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

        val invalidFieldStream2 = io.reactivex.Observable.combineLatest(
            cnsltStream,
            ProvinceStream,
            municipalityStream,
            brngyStream,
            hnStream,
            { cnsltInvalid: Boolean, provinceInvalid: Boolean, municipalityInvalid: Boolean, brngyInvalid: Boolean, hnInvalid: Boolean ->
                !cnsltInvalid && !provinceInvalid && !municipalityInvalid && !brngyInvalid && !hnInvalid
            })
        invalidFieldStream2.subscribe { isValid ->
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
            Toast.makeText(this@DoctorRegister, "Please wait...", Toast.LENGTH_SHORT).show()

            registerDoctor()
        }
        binding.haveAccBTN.setOnClickListener {
            startActivity(Intent(this, UserLogin::class.java))
        }


        // Checkboxes setup
        binding.checkboxF2f.setOnCheckedChangeListener { _, isChecked ->
            f2fChecked = isChecked
        }

        binding.checkboxOnline.setOnCheckedChangeListener { _, isChecked ->
            onlineChecked = isChecked
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
    private fun registerDoctor() {


        // Show loading dialog
        val loadingDialog = createLoadingDialog()
        loadingDialog.show()


        val FName = binding.FNameRegEditText.text.toString().trim()
        val LName = binding.LNameRegEditText.text.toString().trim()
        val phoneNumber = binding.PhoneNumRegEditText.text.toString().trim()
        val gender = binding.genderEditText.text.toString().trim()
        val email = binding.emailRegEditText.text.toString().trim()
        val age = binding.AgeRegEditText.text.toString().trim().toInt()
        val specialty = binding.specialityEditText.text.toString().trim()
        val md = binding.mdEditText.text.toString().trim()
        val consultPrice = binding.cnsltPriceEditText.text.toString().trim()
        val province = binding.provinceEditText.text.toString().trim()
        val municipality = binding.municipalityEditText.text.toString().trim()
        val barangay = binding.brgyEditText.text.toString().trim()
        val hn = binding.hnEditText.text.toString().trim()
        val password = binding.PasswordRegEditText.text.toString().trim()
        val f2f = f2fChecked
        val online = onlineChecked


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
                    val call = RetrofitClient.getService().registerDoctor(
                        role = RequestBody.create("text/plain".toMediaTypeOrNull(), "Patient"),
                        fName = RequestBody.create("text/plain".toMediaTypeOrNull(), FName),
                        lName = RequestBody.create("text/plain".toMediaTypeOrNull(), LName),
                        phoneNumber = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber),
                        gender = RequestBody.create("text/plain".toMediaTypeOrNull(), gender),
                        email = RequestBody.create("text/plain".toMediaTypeOrNull(), email),
                        age = RequestBody.create("text/plain".toMediaTypeOrNull(), age.toString()),
                        specialty = RequestBody.create("text/plain".toMediaTypeOrNull(), specialty),
                        md = RequestBody.create("text/plain".toMediaTypeOrNull(), md.toString()),
                        consultPrice = RequestBody.create("text/plain".toMediaTypeOrNull(), consultPrice.toString()),
                        province = RequestBody.create("text/plain".toMediaTypeOrNull(), province),
                        municipality = RequestBody.create("text/plain".toMediaTypeOrNull(), municipality),
                        barangay = RequestBody.create("text/plain".toMediaTypeOrNull(), barangay),
                        hn = RequestBody.create("text/plain".toMediaTypeOrNull(), hn.toString()),
                        password = RequestBody.create("text/plain".toMediaTypeOrNull(), password),
                        f2f = RequestBody.create("text/plain".toMediaTypeOrNull(), f2f.toString()), // Pass f2f here
                        online = RequestBody.create("text/plain".toMediaTypeOrNull(), online.toString()), // Pass online here
                        image = imagePart
                    )

                    call.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@DoctorRegister, "Patient registered successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@DoctorRegister, UserLogin::class.java))
                                finish()
                            } else {
                                val errorMessage = "Registration failed: ${response.errorBody()?.string()}"
                                Toast.makeText(this@DoctorRegister, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(this@DoctorRegister, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@DoctorRegister, UserLogin::class.java))
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



    private fun showToast(gender: String) {
        Toast.makeText(this, "You chose $gender", Toast.LENGTH_SHORT).show()
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
        binding.PhoneNumRegEditText.error = if (isNotValid) "Cannot exceed 10 characters" else null
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

    // SPECIALITY ALERT
    private fun showSpecialityExistAlert(isNotValid: Boolean) {
        binding.specialityEditText.error = if (isNotValid) "Invalid Speciality" else null
    }
    // MD ALERT
    private fun showmdExistAlert(isNotValid: Boolean) {
        binding.mdEditText.error = if (isNotValid) "Invalid MD Year" else null
    }
    // CONSULT ALERT
    private fun cnsltExistAlert(isNotValid: Boolean) {
        binding.cnsltPriceEditText.error = if (isNotValid) "Invalid Consultation Price" else null
    }
    // PROVINCE ALERT
    private fun provinceExistAlert(isNotValid: Boolean) {
        binding.provinceEditText.error = if (isNotValid) "Invalid Province" else null
    }
    // MUNICIPALITY ALERT
    private fun municipalityExistAlert(isNotValid: Boolean) {
        binding.municipalityEditText.error = if (isNotValid) "Invalid Municipality" else null
    }
    // BARANGGAY ALERT
    private fun brngyExistAlert(isNotValid: Boolean) {
        binding.brgyEditText.error = if (isNotValid) "Invalid Baranggay" else null
    }
    // HOUSE NUMBER ALERT
    private fun hnExistAlert(isNotValid: Boolean) {
        binding.hnEditText.error = if (isNotValid) "Invalid House Number" else null
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