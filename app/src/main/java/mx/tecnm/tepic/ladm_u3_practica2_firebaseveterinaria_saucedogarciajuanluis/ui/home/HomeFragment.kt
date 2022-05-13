package mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    val baseRemota=FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnAgregarPropietario.setOnClickListener {
            if(binding.etCurp.text.toString().equals("")||binding.etNombre.text.toString().equals("")
                ||binding.etTelefono.text.toString().equals("") ||binding.etEdad.text.toString().equals("")){
                Toast.makeText(requireContext(), "Inserte todos los datos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val insertar = hashMapOf(
                "curp" to binding.etCurp.text.toString(),
                "nombre" to binding.etNombre.text.toString(),
                "telefono" to binding.etTelefono.text.toString(),
                "edad" to binding.etEdad.text.toString().toInt())

            baseRemota.collection("propietario").add(insertar)
                .addOnSuccessListener {
                    //SE PUDO
                    Toast.makeText(requireContext(), "Se agregó el propietario", Toast.LENGTH_LONG).show()
                    binding.etCurp.setText("")
                    binding.etNombre.setText("")
                    binding.etTelefono.setText("")
                    binding.etEdad.setText("")
                }
                .addOnFailureListener {
                    // NO SE PUDO
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}