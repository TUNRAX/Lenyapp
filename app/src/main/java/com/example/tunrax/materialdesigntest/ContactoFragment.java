package com.example.tunrax.materialdesigntest;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.pm.PackageManager;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Este fragment contiene los datos de los numeros telefonicos del proveedor
 * ademas es posible hacer una llamada al numero telefonico del leñador y ademas
 * con la api de whatsapp es posible enviar mensajes al leñador por whatsapp
 */
public class ContactoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton btnCall1, btnCall2, btnSms1, btnSms2;
    private TextView txtNum1, txtNum2;

    private OnFragmentInteractionListener mListener;

    public ContactoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactoFragment newInstance(String param1, String param2) {
        ContactoFragment fragment = new ContactoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtNum1 = (TextView) getView().findViewById(R.id.txtNum1);
        txtNum2 = (TextView) getView().findViewById(R.id.txtNum2);
        btnCall1 = (ImageButton) getView().findViewById(R.id.btnCall1);
        btnCall2 = (ImageButton) getView().findViewById(R.id.btnCall2);
        btnSms1 = (ImageButton) getView().findViewById(R.id.btnSms1);
        btnSms2 = (ImageButton) getView().findViewById(R.id.btnSms2);

        Bundle b = getActivity().getIntent().getExtras();

        String fono1 = b.getString("fono1");
        String fono2 = b.getString("fono2");

        txtNum1.setText(fono1);
        txtNum2.setText(fono2);

        if(fono1.equals("")){
            btnCall1.setVisibility(View.GONE);
            btnSms1.setVisibility(View.GONE);
            txtNum1.setVisibility(View.GONE);
        }else if (fono2.equals("")){
            btnCall2.setVisibility(View.GONE);
            btnSms2.setVisibility(View.GONE);
            txtNum2.setVisibility(View.GONE);
        }


        btnCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNum1.toString().contains("+")) {
                    String numero = txtNum1.getText().toString();
                    dialContactPhone(numero);
                } else {
                    String numero = txtNum1.getText().toString();
                    String numeroConMas = ("+" + numero);
                    dialContactPhone(numeroConMas);
                }
            }
        });

        btnSms1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numero = txtNum1.getText().toString();
                String replaced = numero.replaceAll("[()]", "");
                String replacedAgain = replaced.replaceAll("[+]", "");
                String replacedAgainAgain = replacedAgain.replaceAll("\\s+","");
                Intent whatsapp = new Intent( Intent.ACTION_VIEW , Uri.parse("https://api.whatsapp.com/send?phone="+replacedAgainAgain) );
                startActivity( whatsapp );
            }
        });

        btnCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNum2.toString().contains("+")) {
                    String numero = txtNum2.getText().toString();
                    dialContactPhone(numero);
                } else {
                    String numero = txtNum2.getText().toString();
                    String numeroConMas = ("+" + numero);
                    dialContactPhone(numeroConMas);
                }
            }
        });

        btnSms2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = txtNum2.getText().toString();
                String replaced = numero.replaceAll("[()]", "");
                String replacedAgain = replaced.replaceAll("[+]", "");
                String replacedAgainAgain = replacedAgain.replaceAll("\\s+","");
                Intent whatsapp = new Intent( Intent.ACTION_VIEW , Uri.parse("https://api.whatsapp.com/send?phone="+replacedAgainAgain) );
                startActivity( whatsapp );
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contacto, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (ContactoFragment.OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
