package mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.ui.ActualizarMascota

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.R
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.ActualizarMascotaFragmentBinding
import java.lang.NullPointerException

class ActualizarMascotaFragment : Fragment() {

    companion object {
        fun newInstance() = ActualizarMascotaFragment()
    }
    lateinit var binding: ActualizarMascotaFragmentBinding
    private lateinit var viewModel: ActualizarMascotaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActualizarMascotaFragmentBinding.inflate(inflater,container,false)
        var idGlobal=""
        var listaID = ArrayList<String>()
        var listaIDPropietario = ArrayList<String>()
        val baseRemota2= FirebaseFirestore.getInstance()


        FirebaseFirestore.getInstance().collection("mascota")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //ERROR
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                listaID.clear()
                val llenar = ArrayList<String>()
                for (documento in query!!) {
                    var cadena =
                        "${documento.getString("nombre")} ${documento.getString("raza")}"
                    llenar.add(cadena)
                    listaID.add(documento.id)
                }




                try {
                    binding.lista.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, llenar)
                    binding.lista.setOnItemClickListener { adapterView, view, pos, l ->
                        val idEl = listaID.get(pos)
                        idGlobal = idEl
                        AlertDialog.Builder(requireContext())
                            .setTitle("Atención")
                            .setMessage("¿Qué desea hacer?")
                            .setNeutralButton("Eliminar") { d, i ->
                                eliminar(idEl)
                            }
                            .setNegativeButton("Actualizar") { d, i ->
                                actualizar(idEl)
                            }
                            .show()
                    }
                } catch (err: NullPointerException) {
                }
            }
        binding.btnActualizar.setOnClickListener {
            try {
                val baseRemota=FirebaseFirestore.getInstance()
                baseRemota.collection("mascota")
                    .document(idGlobal)
                    .update("nombre" , binding.etNombreMas.text.toString(),
                        "raza" , binding.etRaza.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Se actualizó con exito", Toast.LENGTH_LONG)
                            .show()
                        binding.etNombreMas.setText("")
                        binding.etRaza.setText("")
                        binding.etCurp.setText("")
                        binding.etNombreProp.setText("")
                        binding.etTelefono.setText("")
                        binding.etEdad.setText("")
                    }
                    .addOnFailureListener {
                        AlertDialog.Builder(requireContext())
                            .setMessage(it.message)
                            .show()
                    }
            }catch (err: NullPointerException){}
        }




        val root: View=binding.root
        return root
    }

    private fun actualizar(idEl: String) {
        var curp=""
        val baseRemota= FirebaseFirestore.getInstance()
        baseRemota.collection("mascota").document(idEl).get()
            .addOnSuccessListener {
                binding.etNombreMas.setText(it.getString("nombre"))
                binding.etTelefono.setText(it.getString("telefono"))
                binding.etEdad.setText(it.getLong("edad").toString())
                curp=it.getString("curp").toString()
                binding.etNombreProp.setText(it.getString("nombreProp").toString())
                binding.etTelefono.setText(it.getString("telefono").toString())
                binding.etEdad.setText(it.getLong("edad").toString())
                binding.etCurp.setText(it.getString("curp").toString())
                binding.etRaza.setText(it.getString("raza").toString())
                            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(it.message)
                    .show()
            }
        val baseRemota2= FirebaseFirestore.getInstance()

    }

    private fun eliminar(idEl: String) {
        try {
            val baseRemota= FirebaseFirestore.getInstance()
            baseRemota.collection("mascota")
                .document(idEl)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Se eliminó la Mascota", Toast.LENGTH_LONG)
                        .show()
                    binding.etNombreMas.setText("")
                    binding.etRaza.setText("")
                    binding.etCurp.setText("")
                    binding.etNombreProp.setText("")
                    binding.etTelefono.setText("")
                    binding.etEdad.setText("")
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
        }catch (err:NullPointerException){}
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ActualizarMascotaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}