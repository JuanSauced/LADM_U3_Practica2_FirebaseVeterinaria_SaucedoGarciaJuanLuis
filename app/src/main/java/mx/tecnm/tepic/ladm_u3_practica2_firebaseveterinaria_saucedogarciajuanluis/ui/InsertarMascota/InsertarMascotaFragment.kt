package mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.ui.InsertarMascota

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.R
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.ActualizarMascotaFragmentBinding
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.InsertarMascotaFragmentBinding
import java.lang.NullPointerException

class InsertarMascotaFragment : Fragment() {

    companion object {
        fun newInstance() = InsertarMascotaFragment()
    }
    lateinit var binding:InsertarMascotaFragmentBinding

    private lateinit var viewModel: InsertarMascotaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= InsertarMascotaFragmentBinding.inflate(inflater,container,false)
        val root: View=binding.root

        val baseRemota= FirebaseFirestore.getInstance()
        var curpGlobal=""
        var nombreProp=""
        var telefono=""
        var edad=0



        binding.btnBuscarProp.setOnClickListener {
            baseRemota.collection("propietario")
                .whereEqualTo("nombre", binding.etNombreProp.text.toString())
                .addSnapshotListener { query, error ->
                    if(error!= null){
                        AlertDialog.Builder(requireContext())
                            .setMessage(error.message)
                            .show()
                    return@addSnapshotListener
                }
                    curpGlobal=""
                    nombreProp=""
                    telefono=""
                    edad=0
                    for(documento in query!!){
                        curpGlobal = documento.getString("curp").toString()
                        binding.getCurp.setText(documento.getString("curp"))
                        nombreProp=documento.getString("nombre").toString()
                        telefono=documento.getString("telefono").toString()
                        edad=documento.getLong("edad").toString().toInt()
                    }

                }
           /* baseRemota.collection("propietario")
                .whereEqualTo("nombre", binding.etNombreProp.text.toString()).get()
                .addOnSuccessListener {
                    curpGlobal=""
                    nombreProp=""
                    telefono=""
                    edad=0
                    for (documento in it) {
                        curpGlobal = documento.getString("curp").toString()
                        binding.getCurp.setText(documento.getString("curp"))
                        nombreProp=documento.getString("nombre").toString()
                        telefono=documento.getString("telefono").toString()
                        edad=documento.getLong("edad").toString().toInt()
                    }
                }*/
                    binding.btnAgregarMascota.setOnClickListener {
                        if (binding.getCurp.text.toString().equals("Seleccione un propietario")) {
                            Toast.makeText(
                                requireContext(),
                                "Seleccione un propietario",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            try {
                                val baseRemota2 = FirebaseFirestore.getInstance()
                                val insertar = hashMapOf(
                                    "nombre" to binding.etNombre.text.toString(),
                                    "raza" to binding.etRaza.text.toString(),
                                    "curp" to curpGlobal.toString(),
                                    "nombreProp" to nombreProp.toString(),
                                    "telefono" to telefono.toString(),
                                    "edad" to edad.toString().toInt()
                                )
                                baseRemota2.collection("mascota").add(insertar)
                                    .addOnSuccessListener {
                                        // SÍ SE PUDO
                                        Toast.makeText(
                                            requireContext(),
                                            "Se agregó la mascota",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        binding.etNombreProp.setText("")
                                        binding.etNombre.setText("")
                                        binding.etRaza.setText("")
                                        binding.getCurp.setText("Seleccione un propietario")
                                    }
                                    .addOnFailureListener {
                                        // NO SE PUDO
                                        AlertDialog.Builder(requireContext())
                                            .setMessage(it.message)
                                            .show()
                                    }
                            }catch (err:NullPointerException){}
                        }
                    }

        }




        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InsertarMascotaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}