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

        var listaID = ArrayList<String>()
        val baseRemota= FirebaseFirestore.getInstance()
        var idGlobal=""
        var curpGlobal=""


        FirebaseFirestore.getInstance().collection("propietario")
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
                        "${documento.getString("curp")} ${documento.getString("nombre")}  ${
                            documento.getLong("edad")
                        } años"
                    llenar.add(cadena)
                    listaID.add(documento.id)
                }
                try {
                    binding.lista.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, llenar)
                    binding.lista.setOnItemClickListener { adapterView, view, pos, l ->
                        val idEl = listaID.get(pos)
                        idGlobal = idEl
                    }
                } catch (err: NullPointerException) {
                }
            }
        binding.btnBuscarProp.setOnClickListener {
            baseRemota.collection("propietario")
                .whereEqualTo("nombre", binding.etNombreProp.text.toString()).get()
                .addOnSuccessListener {
                    lateinit var curp: String
                    for (documento in it) {
                        curpGlobal = documento.getString("curp").toString()
                        binding.getCurp.setText(documento.getString("curp"))
                    }
                }
                    binding.btnAgregarMascota.setOnClickListener {
                        val baseRemota2 = FirebaseFirestore.getInstance()
                        baseRemota2.collection("mascota").get()
                            .addOnSuccessListener {
                                for (documento in it) {
                                    val datos = hashMapOf(
                                        "nombre" to binding.etNombre.text.toString(),
                                        "raza" to binding.etRaza.text.toString(),
                                        "curp" to curpGlobal
                                    )
                                    baseRemota2.collection("mascota").add(datos)
                                        .addOnSuccessListener{
                                            // SÍ SE PUDO
                                            Toast.makeText(requireContext(), "Exito! Sí se pudo", Toast.LENGTH_LONG).show()
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
                                }
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