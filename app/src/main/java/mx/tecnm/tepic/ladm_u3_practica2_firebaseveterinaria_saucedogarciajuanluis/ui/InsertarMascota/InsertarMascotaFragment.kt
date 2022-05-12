package mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.ui.InsertarMascota

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.R
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.ActualizarMascotaFragmentBinding
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.InsertarMascotaFragmentBinding

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
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InsertarMascotaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}