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
            this.customerNumber = customer.number;

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
                        invoiceNumber = v;
                        break;
                    case "ITM_NUMBER":
                        itemNumber = v;
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

        public override String ToString()
        {
            return
                    "Rechnungsnummer:\t" + invoiceNumber + "\n" +
                    "Kundennummer:\t\t" + customerNumber + "\n" +
                    "Kunde:\t\t\t" + customerName + "\n" +
                    "Artikelnummer:\t\t" + itemNumber + "\n" +
                    "Artikelbeschreibung:\t" + itemName + "\n" +
                    "Angeforderte Menge:\t" + requiredQuantity + "\n" +
                    "Gelieferte Menge:\t" + deliveredQuantity + "\n" +
                    "Rechnungsdatum:\t\t" + invoiceDate + "\n" +
                    "Faelligkeitsdatum:\t" + requiredDate + "\n" +
                    "Stueckpreis Netto:\t" + netPrice + "\n" +
                    "Stueckpreis Brutto:\t" + taxPrice + "\n" +
                    "Gesamtbetrag Netto:\t" + netTotal + "\n" +
                    "Gesamtbetrag Brutto: \t" + taxTotal + "\n" +
                    "Waehrung:\t\t" + currency + "\n";
        }
    }
}
