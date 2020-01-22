package com.monitoryourexpenses.expenses.loginuser


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.databinding.LoginUserFragmentBinding
import com.monitoryourexpenses.expenses.utilites.PrefManager
import com.monitoryourexpenses.expenses.utilites.isConnected
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginUserFragment : Fragment() {


    private lateinit var binding: LoginUserFragmentBinding
    private val signIn = 9001
    private var mGoogleSignInClient: GoogleSignInClient? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        if(context?.let { PrefManager.isLoggedIn(it) }!! && context?.let { PrefManager.isRegistered(it) }!!){
            findNavController().navigate(R.id.action_loginUserFragment_to_myExpenseFragment)
        }
        if (context?.let { PrefManager.isLoggedIn(it) }!! && !context?.let { PrefManager.isRegistered(it) }!!){
            findNavController().navigate(R.id.action_loginUserFragment_to_registerationUserFragment)
        }



        binding = DataBindingUtil.inflate(inflater, R.layout.login_user_fragment,container,false)


        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }


        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginUserViewModelFactory(application)
        val viewModel = ViewModelProviders.of(this,viewModelFactory)
            .get(LoginUserViewModel::class.java)



        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.loginButton.setOnClickListener {
            if(isConnected()){
                signIn()
            }else{
                Toast.makeText(context,getString(R.string.weak_internet_connection),Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == signIn) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if(account != null) {
                context?.let { PrefManager.saveEmail(it, account.email.toString()) }
                context?.let { PrefManager.savePhoto(it,account.photoUrl.toString()) }
                context?.let { PrefManager.saveName(it,account.givenName.toString()) }
                // Signed in successfully, show authenticated UI.
                context?.let { PrefManager.setLoggedIn(it,true) }
                if (context?.let { PrefManager.isRegistered(it) }!!){
                    findNavController().navigate(R.id.action_loginUserFragment_to_myExpenseFragment)
                }else{
                    findNavController().navigate(R.id.action_loginUserFragment_to_registerationUserFragment)
                }
            }

        } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
            Log.w("tag", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, signIn)
    }



}
