<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="ERP_WebInterface._Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">

    <div class="jumbotron">
        <h1>BMMR Kundenportal</h1>
        <p>&nbsp;</p>
        <p class="lead">Melden Sie sich mit Ihrer Kundennummer und Passwort an um Ihre Rechnungen einzusehen.</p>
        <p class="lead">&nbsp;</p>
        <p class="lead">Kundennummer:&nbsp;&nbsp;&nbsp;&nbsp;
            <asp:TextBox ID="customerTextBox" runat="server"></asp:TextBox>
        </p>
        <p class="lead">&nbsp;Passwort:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <asp:TextBox ID="passwordTextBox" runat="server"></asp:TextBox>
        </p>
        <p>
            <asp:Button ID="login" runat="server" OnClick="Button1_Click" Text="Anmelden" />
        </p>
        <p>
            <asp:Label ID="errorLabel" runat="server"></asp:Label>
        </p>
    </div>

    </asp:Content>
