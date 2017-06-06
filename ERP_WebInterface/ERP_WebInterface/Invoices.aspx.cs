using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.HtmlControls;

namespace ERP_WebInterface
{
    public partial class _Invoices : Page
    {


        protected void Page_Load(object sender, EventArgs e)
        {
            customerNumberLabel.Text = _Default.customer.number.TrimStart(new char[] { '0' });
            customerNameLabel.Text = _Default.customer.firstName;
            customerStreetLabel.Text = _Default.customer.street;
            customerCityLabel.Text = _Default.customer.city;

            foreach (var invoice in _Default.customer.Invoices)
            {
                var b = new Button();
                b.Click += (s, args) =>
                {
                    var path = Util.CreatePDF(invoice);
                    Response.ContentType = "application/octet-stream";
                    Response.AppendHeader("Content-Disposition", $"attachment; filename={path}");
                    Response.TransmitFile(Server.MapPath($"~/{path}"));
                    Response.End();

                };
                b.Text = "als PDF herunterladen";
                var p = new Panel();
                var div = new HtmlGenericControl("div");
                div.Attributes.Add("id", invoice.invoiceNumber);
                div.InnerHtml = invoice.HTMLString()+ "<br>";
                p.Controls.Add(div);
                p.Controls.Add(b);
                p.BorderStyle = BorderStyle.Solid;
                invoicesPanel.Controls.Add(p);

                var div2 = new HtmlGenericControl("div");
                div2.Attributes.Add("id", invoice.invoiceNumber + "2");
                div2.InnerHtml = "<br><br>";
                invoicesPanel.Controls.Add(div2);
            }
        }

      
    }
}