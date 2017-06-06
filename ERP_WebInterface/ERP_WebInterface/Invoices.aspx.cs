using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ERP_WebInterface
{
    public partial class _Invoices : Page
    {
        override protected void OnInit(EventArgs e)
        {
            Load += Page_Load;
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            customerNumberLabel.Text = _Default.customer.number;
            customerNameLabel.Text = _Default.customer.firstName;
            customerStreetLabel.Text = _Default.customer.street;
            customerCityLabel.Text = _Default.customer.city;

            invoicesListBox.Items.AddRange(_Default.customer.Invoices.Select(i => new ListItem(i.invoiceNumber, i.ToString())).ToArray());
        }

      
    }
}