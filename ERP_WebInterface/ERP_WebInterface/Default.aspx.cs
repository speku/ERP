using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ERP_WebInterface
{
    public partial class _Default : Page
    {
        public static Connector connector = new Connector();
        public static Customer customer;

        

        protected void Page_Load(object sender, EventArgs e)
        {
            
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            customer = connector.Customer(customerTextBox.Text);
            if (customer.firstName == null || customer.firstName == "")
            {
                errorLabel.Text = $"Ein Kunde mit der Kundennummer {customerTextBox.Text} konnte nicht gefunden werden.\nBitte überprüfen Sie Ihre Eingabe.";
                customerTextBox.Text = "";
                passwordTextBox.Text = "";
                errorLabel.ForeColor = System.Drawing.Color.Red;
            } else
            {
                errorLabel.Text = $"{customer.firstName} wurde erfolgreich angemeldet!";
                errorLabel.ForeColor = System.Drawing.Color.Green;
                Response.Redirect("~/Invoices.aspx");
            }
        }
    }
}