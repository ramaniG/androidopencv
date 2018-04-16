package gohsinhee.imagefilter5;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageFilter extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 1999;
    final Context context = this;
    private ImageView imageViewDisplay;
    private ImageView imageViewOriginal;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);

        // Setting up the Image Holder
        this.imageViewDisplay = this.findViewById(R.id.imageView);
        this.imageViewOriginal = this.findViewById(R.id.imageView2);
        this.imageViewOriginal.setVisibility(View.INVISIBLE);

        // Action for take photo button
        Button btntakephoto = this.findViewById(R.id.btn_takephoto);
        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        // Action for photo from library button
        Button btntakelibrary = this.findViewById(R.id.btn_takelibrary);
        btntakelibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        });

        // Action for saving photo
        Button btnsave = findViewById(R.id.btn_savephoto);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                img.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(destination));
                sendBroadcast(intent);
            }
        });

        // Action for reset
        Button btnreset = findViewById(R.id.btn_reset);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap img = ((BitmapDrawable) imageViewOriginal.getDrawable()).getBitmap();
                imageViewDisplay.setImageBitmap(img);
            }
        });

        // Action for Gaussian Filter
        Button btnGaussian = findViewById(R.id.btn_gaussian);
        btnGaussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_gaussian);
                dialog.setTitle("Gaussian Filter");

                // Button filter 1
                Button btnfilter1 = dialog.findViewById(R.id.btn_gfilter1);
                btnfilter1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.GaussianBlur(source, dest, new Size(3, 3), 0.5);

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 2
                Button btnfilter2 = dialog.findViewById(R.id.btn_gfilter2);
                btnfilter2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.GaussianBlur(source, dest, new Size(9, 9), 1);

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 3
                Button btnfilter3 = dialog.findViewById(R.id.btn_gfilter3);
                btnfilter3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.GaussianBlur(source, dest, new Size(25, 25), 3);

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 4
                Button btncustom = dialog.findViewById(R.id.btn_gcustom);
                final TextView txta = dialog.findViewById(R.id.txt_ga);
                final TextView txts = dialog.findViewById(R.id.txt_gs);

                btncustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.GaussianBlur(source, dest, new Size(Integer.valueOf(txta.getText().toString()),
                                Integer.valueOf(txta.getText().toString())), Float.valueOf(txts.getText().toString()));

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // Action for Laplacian
        Button btnlaplacian = findViewById(R.id.btn_laplacian);
        btnlaplacian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                Mat source = new Mat();
                Mat dest = new Mat();
                Utils.bitmapToMat(img, source);
                Imgproc.cvtColor(source, source, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.Laplacian(source, dest, source.depth());
                Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(dest, btmp);
                imageViewDisplay.setImageBitmap(btmp);
            }
        });

        // Action for Median
        Button btnmedian = findViewById(R.id.btn_median);
        btnmedian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_median);
                dialog.setTitle("Median Filter");

                // Button filter 1
                Button btnfilter1 = dialog.findViewById(R.id.btn_mfilter1);
                btnfilter1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.medianBlur(source, dest, 3);

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 2
                Button btnfilter2 = dialog.findViewById(R.id.btn_mfilter2);
                btnfilter2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.medianBlur(source, dest, 5);

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 3
                Button btnfilter3 = dialog.findViewById(R.id.btn_mfilter3);
                btnfilter3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.medianBlur(source, dest, 9);

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter Custom
                Button btncustom = dialog.findViewById(R.id.btn_mcustom);
                final TextView txta = dialog.findViewById(R.id.txt_ma);

                btncustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.medianBlur(source, dest, Integer.valueOf(txta.getText().toString()));

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // Action for median
        Button btnaverage = findViewById(R.id.btn_average);
        btnaverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_average);
                dialog.setTitle("Average Filter");

                // Button filter 1
                Button btnfilter1 = dialog.findViewById(R.id.btn_afilter1);
                btnfilter1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.blur(source, dest, new Size(3, 3));

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 2
                Button btnfilter2 = dialog.findViewById(R.id.btn_afilter2);
                btnfilter2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.blur(source, dest, new Size(9, 9));

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 3
                Button btnfilter3 = dialog.findViewById(R.id.btn_afilter3);
                btnfilter3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.blur(source, dest, new Size(25, 25));

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter 4
                Button btnfilter4 = dialog.findViewById(R.id.btn_afilter4);
                btnfilter4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.blur(source, dest, new Size(50, 50));

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                // Button filter Custom
                Button btncustom = dialog.findViewById(R.id.btn_acustom);
                final TextView txta = dialog.findViewById(R.id.txt_aa);

                btncustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap img = ((BitmapDrawable) imageViewDisplay.getDrawable()).getBitmap();
                        Mat source = new Mat();
                        Mat dest = new Mat();
                        Utils.bitmapToMat(img, source);

                        Imgproc.blur(source, dest, new Size(Integer.valueOf(txta.getText().toString()), Integer.valueOf(txta.getText().toString())));

                        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(dest, btmp);
                        imageViewDisplay.setImageBitmap(btmp);

                        // Close dialog after applying filter
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageViewOriginal.setImageBitmap(photo);
            imageViewDisplay.setImageBitmap(photo);
        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Bitmap photo = null;
            if (data != null) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            imageViewOriginal.setImageBitmap(photo);
            imageViewDisplay.setImageBitmap(photo);
        }
    }
}
