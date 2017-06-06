using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ERP_WebInterface
{
    public class Invoice
    {
        public Customer customer;
        public Boolean valid = false; // wenn keine Rechnungsinfos gefunden werden konnten: valid = false

        public String
        invoiceNumber,
        customerNumber,
        customerName,
        itemNumber, // Artikelnummer
        itemName, // Artikelbeschreibung
        requiredQuantity, // angeforderte Menge?
        deliveredQuantity, // ausgelieferte Menge
        invoiceDate, // Rechnungsdatum
        requiredDate, // Faelligkeitsdatum?
        netPrice, // netto
        taxPrice, // brutto
        netTotal,
        taxTotal,
        currency; // Waehrung

        public Dictionary<String, String> details;


        public Invoice(Customer customer, Dictionary<String, String> details)
        {
            this.details = details;
            this.customer = customer;
            this.customerNumber = customer.number.TrimStart(new char[] { '0' });

            if (details == null)
            {
                return;
            }
            else
            {
                valid = true;
            }

            foreach (var kv in details)
            {
                String v = kv.Value;
                switch (kv.Key)
                {
                    case "SD_DOC":
                        invoiceNumber = v.TrimStart(new char[] { '0' });
                        break;
                    case "ITM_NUMBER":
                        itemNumber = v.TrimStart(new char[] { '0' });
                        break;
                    case "SHORT_TEXT":
                        itemName = v;
                        break;
                    case "DOC_DATE":
                        invoiceDate = v;
                        break;
                    case "REQ_QTY":
                        requiredQuantity = v;
                        break;
                    case "REQ_DATE":
                        requiredDate = v;
                        break;
                    case "NAME":
                        customerName = v;
                        break;
                    case "DLV_QTY":
                        deliveredQuantity = v;
                        break;
                    case "NET_PRICE":
                        netPrice = v;
                        break;
                    case "CURRENCY":
                        currency = v;
                        break;
                }
            }

            Total();

            //List<Dictionary<string, string>> orderDetails = customer.GetOrderDetails(invoiceNumber);
            //if (orderDetails != null)
            //{
            //    foreach (var detail in orderDetails)
            //    {
            //        String val = detail["TAX_VALUE"];
            //        if (val != null)
            //        {
            //            taxPrice = val;
            //            break;
            //        }
            //    }
            //}
        }

        void Total()
        {
            try
            {
                var netAmount = float.Parse(netPrice);
                taxPrice = netAmount * 1.19 + "";
                var netTotal = netAmount * float.Parse(deliveredQuantity);
                taxTotal = netTotal * 1.19 + "";
                this.netTotal = netTotal + "";
            } catch { }


            //var netAmount = float.Parse(string.Join("", netPrice.Where(c => Char.IsDigit(c) || Char.IsPunctuation(c)).ToArray()));
        }


        public override string ToString()
        {
            return
                    "Rechnungsnummer:\t" + invoiceNumber + Environment.NewLine +
                    "Kundennummer:\t\t" + customerNumber + Environment.NewLine +
                    "Kunde:\t\t\t" + customerName + Environment.NewLine +
                    "Artikelnummer:\t\t" + itemNumber + Environment.NewLine +
                    "Artikelbeschreibung:\t" + itemName + Environment.NewLine +
                    "Angeforderte Menge:\t" + requiredQuantity + Environment.NewLine +
                    "Gelieferte Menge:\t" + deliveredQuantity + Environment.NewLine +
                    "Rechnungsdatum:\t\t" + invoiceDate + Environment.NewLine +
                    "Faelligkeitsdatum:\t" + requiredDate + Environment.NewLine +
                    "Stueckpreis Netto:\t" + netPrice + Environment.NewLine +
                    "Stueckpreis Brutto:\t" + taxPrice + Environment.NewLine +
                    "Gesamtbetrag Netto:\t" + netTotal + Environment.NewLine +
                    "Gesamtbetrag Brutto: \t" + taxTotal + Environment.NewLine +
                    "Währung:\t\t" + currency + Environment.NewLine;
        }

        char space = '\u00a0';

        public string PDFString()
        {
            return
                    "\nRechnungsnummer: " + space + invoiceNumber + Environment.NewLine +
                    "Kundennummer: " + space + customerNumber + Environment.NewLine +
                    "Kunde: " + space + Environment.NewLine +
                    "Artikelnummer: " + itemNumber + Environment.NewLine +
                    "Artikelbeschreibung: " + itemName + Environment.NewLine +
                    "Angeforderte Menge: " + requiredQuantity + Environment.NewLine +
                    "Gelieferte Menge: " + deliveredQuantity + Environment.NewLine +
                    "Rechnungsdatum: " + invoiceDate + Environment.NewLine +
                    "Faelligkeitsdatum: "+ requiredDate + Environment.NewLine +
                    "Stueckpreis Netto: " + netPrice + Environment.NewLine +
                    "Stueckpreis Brutto: " + taxPrice + Environment.NewLine +
                    "Gesamtbetrag Netto: " + netTotal + Environment.NewLine +
                    "Gesamtbetrag Brutto: " + taxTotal + Environment.NewLine +
                    "Währung: " + currency + Environment.NewLine;
        }

        string lb = "<br>";

        public string HTMLString()
        {
            return
                    "Rechnungsnummer:\t" + invoiceNumber + lb +
                    "Kundennummer:\t\t" + customerNumber + lb +
                    "Kunde:\t\t\t" + customerName + lb +
                    "Artikelnummer:\t\t" + itemNumber + lb +
                    "Artikelbeschreibung:\t" + itemName + lb +
                    "Angeforderte Menge:\t" + requiredQuantity + lb +
                    "Gelieferte Menge:\t" + deliveredQuantity + lb +
                    "Rechnungsdatum:\t\t" + invoiceDate + lb +
                    "Faelligkeitsdatum:\t" + requiredDate + lb +
                    "Stueckpreis Netto:\t" + netPrice + lb +
                    "Stueckpreis Brutto:\t" + taxPrice + lb +
                    "Gesamtbetrag Netto:\t" + netTotal + lb +
                    "Gesamtbetrag Brutto: \t" + taxTotal + lb +
                    "Währung:\t\t" + currency + lb;
        }
    }
}
