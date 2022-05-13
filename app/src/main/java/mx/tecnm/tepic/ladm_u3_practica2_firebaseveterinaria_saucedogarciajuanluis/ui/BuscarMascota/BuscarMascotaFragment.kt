package mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.ui.BuscarMascota

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
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.BuscarMascotaFragmentBinding
import java.lang.NullPointerException

class BuscarMascotaFragment : Fragment() {

    companion object {
        fun newInstance() = BuscarMascotaFragment()
    }
    lateinit var binding: BuscarMascotaFragmentBinding

    private lateinit var viewModel: BuscarMascotaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= BuscarMascotaFragmentBinding.inflate(inflater,container,false)
        val root: View=binding.root

        var idGlobal=""
        var listaID = ArrayList<String>()
        val baseRemota2= FirebaseFirestore.getInstance()
        binding.btnBuscarMascota.setOnClickListener{
            var select =baseRemota2.collection("mascota")

            if(binding.etNombre.text.toString()==""){
                if(binding.etPropietario.text.toString()==""){
                    if (binding.etRaza.text.toString()==""){
                        Toast.makeText(requireContext(),"Ingrese un parametro de busqueda", Toast.LENGTH_LONG)
                            .show()
                    }else{
                        var select=baseRemota2.collection("mascota").whereEqualTo("raza",binding.etRaza.text.toString())
                        mostrarLista(select)
                    }
                }else{
                    var select=baseRemota2.collection("mascota").whereEqualTo("curp",binding.etPropietario.text.toString())
                    mostrarLista(select)
                }
            }else{
                var select=baseRemota2.collection("mascota").whereEqualTo("nombre",binding.etNombre.text.toString())
                mostrarLista(select)
            }
        }


        return root
    }
    fun mostrarLista(qur : Query) {
        qur.addSnapshotListener { query, error ->
            if (error != null) {
                //ERROR
                AlertDialog.Builder(requireContext())
                    .setMessage(error.message)
                    .show()
                return@addSnapshotListener
            }
            val llenar = ArrayList<String>()
            for (documento in query!!) {
                var cadena =
                    "${documento.getString("nombre")} ${documento.getString("raza")} "
                llenar.add(cadena)
            }
            binding.lista.adapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, llenar)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BuscarMascotaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}