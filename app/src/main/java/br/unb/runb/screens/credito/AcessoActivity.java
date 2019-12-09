package br.unb.runb.screens.credito;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.onbarcode.barcode.android.AndroidColor;
import com.onbarcode.barcode.android.AndroidFont;
import com.onbarcode.barcode.android.Code39;
import com.onbarcode.barcode.android.IBarcode;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.unb.runb.R;
import br.unb.runb.models.User;
import br.unb.runb.util.UiFunctions;

public class AcessoActivity extends AppCompatActivity {

    private TextView textName;
    private TextView textRA;
    private TextView textCodigo;
    private ImageView imageBarcode;
    private Code39 barcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acesso);

        findViewItems();

        barcode = new Code39();

        getBarcodeData();
    }

    private void findViewItems() {

        textName = findViewById(R.id.text_user_name);
        textRA = findViewById(R.id.text_user_ra);
        textCodigo = findViewById(R.id.text_user_codigo);
        imageBarcode = findViewById(R.id.image_barcode);

        textName.setText(User.getInstance().getName());
        textRA.setText(User.getInstance().getMatricula());
        textCodigo.setText(User.getInstance().getId());

    }

    private void getBarcodeData() {

        AndroidNetworking
                .get("https://homologaservicos.unb.br/dados/administrativo/ru/pessoa/{id}/codigo?access_token={access_token}")
                .addPathParameter("id", User.getInstance().getId())
                .addPathParameter("access_token", User.getInstance().getAccessToken())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.encodeBitmap(response, BarcodeFormat.CODE_39, 600, 120);
                            imageBarcode.setImageBitmap(bitmap);
                        } catch(Exception e) {

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        int statusCode = anError.getErrorCode();
                        try {
                            JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                            if (jsonObject.getString("error").equalsIgnoreCase("access_denied")) {
                                //                            //token expirou
                                //TODO: testar
                                UiFunctions.tokenExpired(AcessoActivity.this);
                                finish();
                            } else if (jsonObject.getString("error").equalsIgnoreCase("validation")) {
                                //nomeRefeicao.setText("Erro no servidor");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


}
