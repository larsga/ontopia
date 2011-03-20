<%@ page language="java" isErrorPage="true" %>
<%@ taglib uri='http://psi.ontopia.net/jsp/taglib/template' prefix='template' %>
<%@ taglib uri='http://psi.ontopia.net/jsp/taglib/framework' prefix='framework' %>
<framework:response/>

<%-- Omnigator: Error 404: 'File not found.' --%>
<%-- $Id: error-file-not-found.jsp,v 1.3 2007/09/14 11:13:57 geir.gronmo Exp $ --%>

<template:insert template='views/template_no_frames.jsp'>

  <template:put name='title' body='true'>File Not Found</template:put>
  <template:put name='heading' body='true'>
      <h1 class='boxed'>Warning</h1>
  </template:put>
  <template:put name='toplinks' body='true'>
    <a href="/">Back to the Welcome Page.</a>
  </template:put>
  
  <template:put name='navigation' body='true'>
   <center>
     <table border="0" with="100%"><tr><td>
        <img src='images/baustelle.gif' width="80" height="90" />
      </td></tr></table>
    </center>
  </template:put>
  <template:put name='content' body='true'>
    <h2>
      The requested file<br>
      <font color="red"><%= request.getAttribute("javax.servlet.error.request_uri") %></font><br>
      could not be found.
    </h2>
  </template:put>

  <%-- Constants --%>
  <template:put name='application' content='/fragments/application.jsp'/>
  <template:put name='header-tagline' content='/fragments/tagline-header.jsp'/>
  <template:put name='footer-tagline' content='/fragments/tagline-footer.jsp'/>

  <%-- Unused --%>
  <template:put name='intro' body='true'></template:put>
  <template:put name='outro' body='true'></template:put>
  <template:put name='head' body='true'></template:put>

</template:insert>
