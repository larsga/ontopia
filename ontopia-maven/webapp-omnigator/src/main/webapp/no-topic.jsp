<%@ page language="java" isErrorPage="true"
         import="java.io.StringWriter, java.io.PrintWriter,
            java.util.Properties,
            net.ontopia.topicmaps.nav2.core.NonexistentObjectException" %>
<%@ taglib uri='http://psi.ontopia.net/jsp/taglib/template'  prefix='template'  %>
<%@ taglib uri='http://psi.ontopia.net/jsp/taglib/framework' prefix='framework' %>
<framework:response/>

<%-- Omnigator: Page handling missing object errors  --%>
<%-- $Id: no-topic.jsp,v 1.2 2007/09/14 11:14:41 geir.gronmo Exp $ --%>
  
<template:insert template='/views/template_no_frames.jsp'>

  <template:put name='title' body='true'>[Omnigator] Unknown topic</template:put>
  <template:put name='heading' body='true'>
      <h1 class='boxed'>Omnigator: Unknown topic</h1>
  </template:put>
  <template:put name='toplinks' body='true'>
      <a href="/omnigator/models/index.jsp">Back to Omnigator's Welcome Page.</a>
  </template:put>
  
  <template:put name='navigation' body='true'>
   <center>
     <table border="0" with="100%"><tr><td>
        <img src='/omnigator/images/baustelle.gif' width="80" height="90" />
      </td></tr></table>
    </center>
  </template:put>
  <template:put name='content' body='true'>
      
    <h3 class="boxed"><font color="red"><strong>No such topic!</strong></font></h3>

    <%
      NonexistentObjectException e = (NonexistentObjectException) exception;
    %>

    <p>The Omnigator could not find any topic with ID
    '<%= e.getObjectId() %>' in topic map
    '<%= e.getTopicMapId() %>'. This means that there is something
    wrong with the link you followed.</p>
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
