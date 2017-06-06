<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Invoices.aspx.cs" Inherits="ERP_WebInterface._Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">

    <div class="jumbotron">
        <h1>BMMR Rechnungen</h1>
        <p class="lead">Wilkommen!</p>
        <p class="lead">&nbsp;</p>
        <p class="lead">Kundennummer:&nbsp;&nbsp;&nbsp;
            <asp:Label ID="customerNumberLabel" runat="server"></asp:Label>
        </p>
        <p class="lead">Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <asp:Label ID="customerNameLabel" runat="server"></asp:Label>
        </p>
        <p class="lead">Straße:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <asp:Label ID="customerStreetLabel" runat="server"></asp:Label>
        </p>
        <p class="lead">Ort:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <asp:Label ID="customerCityLabel" runat="server"></asp:Label>
        </p>
        <p class="lead">&nbsp;</p>
        <p class="lead">Ihre Rechnungen: <p class="lead">
            <asp:ListBox ID="invoicesListBox" runat="server" Height="170px" Width="997px"></asp:ListBox>
        </p>
    </div>

    </asp:Content>
