using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SAP.Middleware.Connector;
using iTextSharp.text;
using iTextSharp.text.pdf;
using System.IO;

namespace ERP_WebInterface
{
    static class Util
    {
        public static List<Dictionary<String, String>> Table(IRfcTable table)
        {
            var result = new List<Dictionary<String, String>>();

            foreach (var row in table)
            {
                var rowMap = new Dictionary<String, String>();
                result.Add(rowMap);
                foreach (var column in row)
                {
                    rowMap.Add(column.Metadata.Name, column.GetString());
                }
            }
            return result;
        }

        public static string CreatePDF(Invoice invoice)
        {
            var fileName = invoice.invoiceNumber + ".pdf";
            using (FileStream fs = new FileStream(Path.Combine(@"C:\Users\speku\Documents\ERP\ERP_WebInterface\ERP_WebInterface", fileName), FileMode.Create, FileAccess.Write, FileShare.None))
            {
                Document doc = new Document(PageSize.A4);
                PdfWriter writer = PdfWriter.GetInstance(doc, fs);
                doc.Open();
                var table = new PdfPTable(2);

                table.AddCell("Rechnungsnummer");
                table.AddCell(invoice.invoiceNumber);

                table.AddCell("Rechnungsdatum");
                table.AddCell(invoice.invoiceDate);


                table.AddCell("Fälligkeit");
                table.AddCell(invoice.requiredDate);

                table.AddCell(" ");
                table.AddCell(" ");

                table.AddCell("Kundennummer");
                table.AddCell(invoice.customerNumber);

                table.AddCell("Artikelnummer");
                table.AddCell(invoice.itemNumber);

                table.AddCell("Artikelbeschreibung");
                table.AddCell(invoice.itemName);

                table.AddCell("Angeforderte Menge");
                table.AddCell(invoice.requiredQuantity);

                table.AddCell(" ");
                table.AddCell(" ");

                table.AddCell("Gelieferte Menge");
                table.AddCell(invoice.deliveredQuantity);

                table.AddCell("Stueckpreis Netto");
                table.AddCell(invoice.netPrice + " " + invoice.currency);

                table.AddCell("Gesamt Netto");
                table.AddCell(invoice.netTotal + " " + invoice.currency);

                table.AddCell(" ");
                table.AddCell(" ");

                table.AddCell("Gesamt Brutto");
                table.AddCell(invoice.taxTotal + " " + invoice.currency);

                var text = $@"

                {invoice.customer.Organization.ToString()}
                

                {invoice.customer.ToString()}
                
                



                ";
                doc.Add(new Paragraph(text));
                doc.Add(table);
                doc.Add(new Paragraph("\n\nVielen Dank für Ihren Einkauf!"));
                doc.Close();
                writer.Close();
            }
            return fileName;
        }
    }
}
