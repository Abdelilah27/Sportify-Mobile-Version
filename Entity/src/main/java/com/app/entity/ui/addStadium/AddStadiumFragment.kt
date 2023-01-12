package com.app.entity.ui.addStadium

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.entity.EntityMainActivity
import com.app.entity.R
import com.app.entity.databinding.FragmentAddStadiumBinding
import com.app.entity.utils.ConstUtil.TEXTINPUTIMAGE
import com.app.entity.utils.NetworkResult
import com.app.entity.utils.PIBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddStadiumFragment : Fragment(R.layout.fragment_add_stadium){
    private val viewModel: AddStadiumViewModel by viewModels()
    lateinit var binding: FragmentAddStadiumBinding
    var pickedPhoto: Uri? = null
    var pickedBitMap: Bitmap? = null
    private var selectedDateTime: Calendar = Calendar.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentAddStadiumBinding.bind(view)
        binding.lifecycleOwner = this
        binding.addStadiumViewModel = viewModel
        initUI(binding)
    }


    @SuppressLint("SetTextI18n")
    private fun initUI(addStadiumBinding: FragmentAddStadiumBinding) {
        // Add new stadium
        addStadiumBinding.addNewStadium.setOnClickListener {
            val price = addStadiumBinding.stadiumPrice.text.toString()
            viewModel.onRegistrationClicked(
                price,
                pickedPhoto
            )
        }

        // To show progressBar when saving data
        viewModel.liveAddStadiumFlow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NetworkResult.Success -> {
                    (activity as PIBaseActivity).dismissProgressDialog("AddStadium")
                    EntityMainActivity.navController.navigateUp()
                }
                is NetworkResult.Error -> {
                    (activity as PIBaseActivity).dismissProgressDialog("AddStadium")
                    //Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    (activity as PIBaseActivity).showProgressDialog("AddStadium")
                }
            }
        })

        // To show the DateTimePicker
        addStadiumBinding.stadiumDisponibilityFrom.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                datePickerListenerFrom,
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        addStadiumBinding.stadiumDisponibilityTo.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                datePickerListenerTo,
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        // To load image from storage
        addStadiumBinding.stadiumImage.setOnClickListener {
            pickPhoto()
        }
    }

    // Handle the date picker events
    private val datePickerListenerFrom = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        selectedDateTime.set(Calendar.YEAR, year)
        selectedDateTime.set(Calendar.MONTH, month)
        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            timePickerListenerFrom,
            selectedDateTime.get(Calendar.HOUR_OF_DAY),
            selectedDateTime.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    // Handle the time picker events
    private val timePickerListenerFrom = TimePickerDialog.OnTimeSetListener { _, hourOfDay, _ ->
        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val selectedDate = dateFormat.format(selectedDateTime.time)
        binding.stadiumDisponibilityFrom.setText("$selectedDate")
    }

    // Handle the date picker events
    private val datePickerListenerTo = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        selectedDateTime.set(Calendar.YEAR, year)
        selectedDateTime.set(Calendar.MONTH, month)
        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            timePickerListenerTo,
            selectedDateTime.get(Calendar.HOUR_OF_DAY),
            selectedDateTime.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    // Handle the time picker events
    private val timePickerListenerTo = TimePickerDialog.OnTimeSetListener { _, hourOfDay, _ ->
        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val selectedDate = dateFormat.format(selectedDateTime.time)
        binding.stadiumDisponibilityTo.setText("$selectedDate")
    }






    //------ Load image from storage
    private fun pickPhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val galeriIntext =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntext, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntext =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                pickedBitMap = if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource(requireActivity().contentResolver, pickedPhoto!!)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        pickedPhoto
                    )
                }
                binding.stadiumImage.setText(TEXTINPUTIMAGE)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //------ End "load image from storage"

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.liveAddStadiumFlow.removeObservers(viewLifecycleOwner)
    }
}
