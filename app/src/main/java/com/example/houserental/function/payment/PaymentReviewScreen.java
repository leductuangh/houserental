package com.example.houserental.function.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.PaymentDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.SettingDAO;
import com.example.houserental.function.view.LockableScrollView;
import com.example.houserental.function.view.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import core.base.BaseMultipleFragment;
import core.util.Constant;
import core.util.Utils;

/**
 * Created by leductuan on 3/14/16.
 */
public class PaymentReviewScreen extends BaseMultipleFragment {

    public static final String TAG = PaymentReviewScreen.class.getSimpleName();
    private static final String PAYMENT_KEY = "payment_key";

    private RoomDAO room;
    private PaymentDAO payment;
    private SettingDAO setting;
    private SimpleDateFormat formatter;
    private LockableScrollView fragment_payment_review_scv_content;
    private LinearLayout fragment_payment_review_ll_signature;
    private SignatureView fragment_payment_review_sv_payer, fragment_payment_review_sv_owner;
    private TextView
            fragment_payment_review_tv_code,
            fragment_payment_review_tv_deposit_total,
            fragment_payment_review_tv_deposit,
            fragment_payment_review_tv_room_unit,
            fragment_payment_review_tv_electric_unit,
            fragment_payment_review_tv_water_unit,
            fragment_payment_review_tv_waste_unit,
            fragment_payment_review_tv_device_unit,
            fragment_payment_review_tv_room_name,
            fragment_payment_review_tv_stay_period,
            fragment_payment_review_tv_owner,
            fragment_payment_review_tv_payer,
            fragment_payment_review_tv_room_price,
            fragment_payment_review_tv_room_price_total,
            fragment_payment_review_tv_electric_price,
            fragment_payment_review_tv_electric_total,
            fragment_payment_review_tv_water_price,
            fragment_payment_review_tv_water_total,
            fragment_payment_review_tv_waste_price,
            fragment_payment_review_tv_waste_total,
            fragment_payment_review_tv_device_price,
            fragment_payment_review_tv_device_total,
            fragment_payment_review_tv_total;
    private LinearLayout fragment_payment_review_ll_content;
    private int screen_width, screen_height, device_total, electric_total, water_total, waste_total, deposit_total, total;

    public static PaymentReviewScreen getInstance(PaymentDAO payment) {
        PaymentReviewScreen screen = new PaymentReviewScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_KEY, payment);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.example.houserental.R.layout.fragment_payment_review, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            payment = (PaymentDAO) bundle.getSerializable(PAYMENT_KEY);
        }
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        room = DAOManager.getRoom(payment.getRoomId());
        setting = DAOManager.getSetting();

        if (room == null || setting == null) {
            Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_payment_review_tv_code = (TextView) findViewById(R.id.fragment_payment_review_tv_code);
        fragment_payment_review_ll_signature = (LinearLayout) findViewById(R.id.fragment_payment_review_ll_signature);
        fragment_payment_review_scv_content = (LockableScrollView) findViewById(R.id.fragment_payment_review_scv_content);
        fragment_payment_review_tv_deposit_total = (TextView) findViewById(R.id.fragment_payment_review_tv_deposit_total);
        fragment_payment_review_tv_deposit = (TextView) findViewById(R.id.fragment_payment_review_tv_deposit);
        fragment_payment_review_tv_room_unit = (TextView) findViewById(R.id.fragment_payment_review_tv_room_unit);
        fragment_payment_review_tv_electric_unit = (TextView) findViewById(R.id.fragment_payment_review_tv_electric_unit);
        fragment_payment_review_tv_water_unit = (TextView) findViewById(R.id.fragment_payment_review_tv_water_unit);
        fragment_payment_review_tv_waste_unit = (TextView) findViewById(R.id.fragment_payment_review_tv_waste_unit);
        fragment_payment_review_tv_device_unit = (TextView) findViewById(R.id.fragment_payment_review_tv_device_unit);
        fragment_payment_review_tv_room_name = (TextView) findViewById(R.id.fragment_payment_review_tv_room_name);
        fragment_payment_review_tv_stay_period = (TextView) findViewById(R.id.fragment_payment_review_tv_stay_period);
        fragment_payment_review_tv_owner = (TextView) findViewById(R.id.fragment_payment_review_tv_owner);
        fragment_payment_review_tv_payer = (TextView) findViewById(R.id.fragment_payment_review_tv_payer);
        fragment_payment_review_tv_room_price = (TextView) findViewById(R.id.fragment_payment_review_tv_room_price);
        fragment_payment_review_tv_room_price_total = (TextView) findViewById(R.id.fragment_payment_review_tv_room_price_total);
        fragment_payment_review_tv_electric_price = (TextView) findViewById(R.id.fragment_payment_review_tv_electric_price);
        fragment_payment_review_tv_electric_total = (TextView) findViewById(R.id.fragment_payment_review_tv_electric_total);
        fragment_payment_review_tv_water_price = (TextView) findViewById(R.id.fragment_payment_review_tv_water_price);
        fragment_payment_review_tv_water_total = (TextView) findViewById(R.id.fragment_payment_review_tv_water_total);
        fragment_payment_review_tv_waste_price = (TextView) findViewById(R.id.fragment_payment_review_tv_waste_price);
        fragment_payment_review_tv_waste_total = (TextView) findViewById(R.id.fragment_payment_review_tv_waste_total);
        fragment_payment_review_tv_device_price = (TextView) findViewById(R.id.fragment_payment_review_tv_device_price);
        fragment_payment_review_tv_device_total = (TextView) findViewById(R.id.fragment_payment_review_tv_device_total);
        fragment_payment_review_tv_total = (TextView) findViewById(R.id.fragment_payment_review_tv_total);
        fragment_payment_review_ll_content = (LinearLayout) findViewById(R.id.fragment_payment_review_ll_content);
        fragment_payment_review_sv_payer = (SignatureView) findViewById(R.id.fragment_payment_review_sv_payer);
        fragment_payment_review_sv_owner = (SignatureView) findViewById(R.id.fragment_payment_review_sv_owner);
        fragment_payment_review_scv_content.blockView = fragment_payment_review_ll_signature;
        findViewById(R.id.fragment_payment_review_im_clear_payer);
        findViewById(R.id.fragment_payment_review_im_clear_owner);
        findViewById(R.id.fragment_payment_review_correct);
        findViewById(R.id.fragment_payment_review_print);
        fragment_payment_review_sv_payer.setDrawingCacheEnabled(true);
        fragment_payment_review_sv_owner.setDrawingCacheEnabled(true);


        final ViewTreeObserver globalLayoutObserver = getView().getViewTreeObserver();
        if (globalLayoutObserver != null) {
            globalLayoutObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (screen_height > 0 && screen_width > 0) {
                        globalLayoutObserver.removeOnGlobalLayoutListener(this);
                        return;
                    }
                    screen_width = fragment_payment_review_ll_content.getWidth();
                    screen_height = fragment_payment_review_ll_content.getHeight();

                }
            });
        }

    }

    @Override
    public void onInitializeViewData() {
        if (payment != null && room != null && setting != null) {

            try {
                Calendar start = Calendar.getInstance();
                start.setTime(payment.getStartDate());
                int dayCountOfMonth = Utils.dayCountOfMonth(start.get(Calendar.MONTH), start.get(Calendar.YEAR));
                int room_deposit = room.getDeposit();
                int min_deposit = setting.getDeposit();

                // quantity
                int stay_days = payment.getStayDays();
                int electric_different = payment.getCurrentElectricNumber() - payment.getPreviousElectricNumber();
                int water_difference = payment.getCurrentWaterNumber() - payment.getPreviousWaterNumber();
                int device_count = payment.getDeviceCount();
                int user_count = payment.getUserCount() <= 2 ? 1 : payment.getUserCount();

                // price
                int electric_price = setting.getElectriPrice();
                int water_price = setting.getWaterPrice();
                int device_price = setting.getDevicePrice();
                int waste_price = user_count <= 2 ? setting.getWastePrice() * 3 : setting.getWastePrice();
                int per_day_room_price = payment.getRoomPrice() / dayCountOfMonth;

                // total
                water_total = water_difference * water_price;
                electric_total = electric_different * electric_price;
                waste_total = user_count * waste_price;
                device_total = device_count * device_price;
                int room_total = stay_days * per_day_room_price;
                deposit_total = min_deposit - room_deposit;
                if (deposit_total < 0)
                    deposit_total = 0;

                fragment_payment_review_tv_code.setText(payment.getCode().toUpperCase());
                fragment_payment_review_tv_stay_period.setText(String.format(getString(com.example.houserental.R.string.payment_review_stay_period_text), formatter.format(payment.getStartDate()), formatter.format(payment.getEndDate().getTime())));
                fragment_payment_review_tv_room_name.setText(payment.getRoomName());
                fragment_payment_review_tv_owner.setText(payment.getOwner());
                fragment_payment_review_tv_payer.setText(payment.getPayer());
                fragment_payment_review_tv_deposit.setText(HouseRentalUtils.toThousandVND(room_deposit));

                String room_unit_text = "";
                if (stay_days >= dayCountOfMonth) {
                    room_unit_text = String.format(getString(R.string.payment_review_room_unit_month_text), 1);
                    room_total = payment.getRoomPrice();
                    fragment_payment_review_tv_room_price.setText(HouseRentalUtils.toThousandVND(payment.getRoomPrice()));
                } else {
                    room_unit_text = String.format(getString(R.string.payment_review_room_unit_day_text), stay_days);
                    fragment_payment_review_tv_room_price.setText(HouseRentalUtils.toThousandVND(per_day_room_price));
                }

                String waste_unit_text = "";
                if (user_count == 1) {
                    waste_unit_text = String.format(getString(R.string.payment_review_waste_unit_room_text), user_count);
                } else {
                    waste_unit_text = String.format(getString(R.string.payment_review_waste_unit_people_text), user_count);
                }

                String electric_unit_text = String.format(getString(R.string.payment_review_electric_unit_text), electric_different);
                String water_unit_text = String.format(getString(R.string.payment_review_water_unit_text), water_difference);

                String device_unit_text = String.format(getString(R.string.payment_review_device_unit_text), device_count);

                int display_total = (water_total + electric_total + waste_total + device_total + room_total + deposit_total);
                total = (water_total + electric_total + waste_total + device_total + room_total);

                fragment_payment_review_tv_room_unit.setText(room_unit_text);
                fragment_payment_review_tv_electric_unit.setText(electric_unit_text);
                fragment_payment_review_tv_water_unit.setText(water_unit_text);
                fragment_payment_review_tv_waste_unit.setText(waste_unit_text);
                fragment_payment_review_tv_device_unit.setText(device_unit_text);

                fragment_payment_review_tv_electric_price.setText(HouseRentalUtils.toThousandVND(electric_price));
                fragment_payment_review_tv_water_price.setText(HouseRentalUtils.toThousandVND(water_price));
                fragment_payment_review_tv_waste_price.setText(HouseRentalUtils.toThousandVND(waste_price));
                fragment_payment_review_tv_device_price.setText(HouseRentalUtils.toThousandVND(device_price));

                fragment_payment_review_tv_room_price_total.setText(HouseRentalUtils.toThousandVND(room_total));
                fragment_payment_review_tv_electric_total.setText(HouseRentalUtils.toThousandVND(electric_total));
                fragment_payment_review_tv_water_total.setText(HouseRentalUtils.toThousandVND(water_total));
                fragment_payment_review_tv_waste_total.setText(HouseRentalUtils.toThousandVND(waste_total));
                fragment_payment_review_tv_device_total.setText(HouseRentalUtils.toThousandVND(device_total));
                fragment_payment_review_tv_deposit_total.setText(HouseRentalUtils.toThousandVND(deposit_total));
                fragment_payment_review_tv_total.setText(HouseRentalUtils.toThousandVND(display_total));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(com.example.houserental.R.string.application_alert_dialog_title), getString(com.example.houserental.R.string.payment_record_no_owner_error), getString(com.example.houserental.R.string.common_ok), null, null);
            finish();
        }
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.payment_review_header));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_review_im_clear_payer:
                fragment_payment_review_sv_payer.clearSignature();
                break;
            case R.id.fragment_payment_review_im_clear_owner:
                fragment_payment_review_sv_owner.clearSignature();
                break;
            case R.id.fragment_payment_review_correct:
                finish();
                break;
            case R.id.fragment_payment_review_print:
                if (isAdded()) {
                    try {
                        boolean result = storeImage();
                        if (result && room != null) {
                            Bitmap payerSignature = fragment_payment_review_sv_payer.getDrawingCache();
                            Bitmap ownerSignature = fragment_payment_review_sv_owner.getDrawingCache();
                            ByteArrayOutputStream ownerSignatureBlob = new ByteArrayOutputStream();
                            ByteArrayOutputStream payerSignatureBlob = new ByteArrayOutputStream();
                            if (payerSignature != null) {
                                payerSignature.compress(Bitmap.CompressFormat.PNG, 100, payerSignatureBlob);
                                payerSignatureBlob.flush();
                                payment.setPayerSignature(payerSignatureBlob.toByteArray());
                            }
                            if (ownerSignature != null) {
                                ownerSignature.compress(Bitmap.CompressFormat.PNG, 100, ownerSignatureBlob);
                                ownerSignatureBlob.flush();
                                payment.setOwnerSignature(ownerSignatureBlob.toByteArray());
                            }
                            payment.setDeviceTotal(device_total);
                            payment.setElectricTotal(electric_total);
                            payment.setWaterTotal(water_total);
                            payment.setWasteTotal(waste_total);
                            payment.setDepositTotal(deposit_total);
                            payment.setTotal(total);
                            payment.save();
                            Calendar new_start_date = Calendar.getInstance();
                            new_start_date.setTime(payment.getEndDate());
                            new_start_date.add(Calendar.DAY_OF_MONTH, 1);
                            room.setPaymentStartDate(new_start_date.getTime());
                            room.setElectricNumber(payment.getCurrentElectricNumber());
                            room.setWaterNumber(payment.getCurrentWaterNumber());
                            room.setDeposit(setting.getDeposit());
                            room.save();
                            replaceFragment(R.id.activity_main_container, PaymentHistoryScreen.getInstance(), PaymentHistoryScreen.TAG, true);
                            payerSignatureBlob.close();
                            ownerSignatureBlob.close();
                            payerSignatureBlob = null;
                            ownerSignatureBlob = null;
                            if (ownerSignature != null)
                                ownerSignature.recycle();
                            if (payerSignature != null)
                                payerSignature.recycle();
                            ownerSignature = null;
                            payerSignature = null;
                        } else {
                            Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Bitmap captureView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private boolean storeImage() {
        try {
            File myDir = new File(Constant.IMAGE_DIRECTORY);
            if (!myDir.exists())
                myDir.mkdir();
            String file_name = myDir.getAbsolutePath() + "/" + payment.getRoomName() + "_" + formatter.format(payment.getEndDate()) + ".jpg";
            File image = new File(file_name);
            if (image.exists()) {
                image.delete();
            } else {
                FileOutputStream out = new FileOutputStream(image);
                captureView(fragment_payment_review_ll_content, screen_width, screen_height).compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isExceptionalView(View view) {
        return super.isExceptionalView(view) || view instanceof SignatureView || view instanceof LockableScrollView;
    }
}
