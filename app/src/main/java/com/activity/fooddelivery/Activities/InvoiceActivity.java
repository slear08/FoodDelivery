package com.activity.fooddelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.fooddelivery.Adapter.ProductInvoiceAdapter;
import com.activity.fooddelivery.Domain.FoodDomain;
import com.activity.fooddelivery.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvEmail;
    private RecyclerView rvProductList;
    private TextView tvTotalAmount,close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_receipt);

        // Retrieve the views
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        rvProductList = findViewById(R.id.rvProductList);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        close = findViewById(R.id.closeTxt);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnDownloadPDF = findViewById(R.id.btnDownloadPDF);
        btnDownloadPDF.setOnClickListener(v -> {
            // Generate and download the PDF
            generatePDF();
        });

        // Set the data
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        ArrayList<FoodDomain> productList = (ArrayList<FoodDomain>) intent.getSerializableExtra("productList");


        tvUsername.setText("Username: " + username);
        tvEmail.setText("Email: " + email);
        // Set up RecyclerView and adapter for productList
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        ProductInvoiceAdapter adapter = new ProductInvoiceAdapter(productList);

        rvProductList.setAdapter(adapter);

        tvTotalAmount.setText("Total Amount: PHP" + calculateTotalAmount() );
    }

    private void generatePDF() {
        // Get the root view of the layout
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        // Create a PDF document
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(rootView.getWidth(), rootView.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Draw the layout onto the PDF page
        rootView.draw(page.getCanvas());

        // Finish the page and document
        document.finishPage(page);

        // Define the file path and name
        Date currentDate = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Format the date to the desired format
        String formattedDate = dateFormat.format(currentDate);

        // Concatenate the date with the file name
        String fileName = "Invoice_" + formattedDate + ".pdf";
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileName;

        // Save the PDF document to the file
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            document.writeTo(outputStream);
            document.close();
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "PDF downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
        }
    }


    private void addInvoiceDetailsToPDF(Document document) throws DocumentException {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        ArrayList<FoodDomain> productList = (ArrayList<FoodDomain>) intent.getSerializableExtra("productList");

        Paragraph paragraph;

        paragraph = new Paragraph("Username: " + username);
        document.add(paragraph);
        paragraph = new Paragraph("Email: " + email);
        document.add(paragraph);

        paragraph = new Paragraph("Product List:");
        paragraph.setSpacingBefore(10f);
        document.add(paragraph);

        for (FoodDomain product : productList) {
            paragraph = new Paragraph(product.getTitle()+ " - PHP" + product.getPrice());
            document.add(paragraph);
        }

        paragraph = new Paragraph("Total Amount: PHP" + calculateTotalAmount());
        paragraph.setSpacingBefore(10f);
        document.add(paragraph);
    }

    private double calculateTotalAmount() {
        Intent intent = getIntent();
        ArrayList<FoodDomain> productList = (ArrayList<FoodDomain>) intent.getSerializableExtra("productList");

        double totalAmount = 0.0;
        for (FoodDomain product : productList) {
            totalAmount += product.getPrice() * product.getCartNumber();
        }

        double TAX = 0.02;
        double delivery = 15;

        double tax = Math.round((totalAmount * TAX) * 100.0) / 100.0;
        double total = Math.round((totalAmount + tax + delivery) * 100.0) / 100.0;

        return total;
    }


}
