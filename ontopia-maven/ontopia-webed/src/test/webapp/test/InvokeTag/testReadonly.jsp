<%@ taglib uri="http://psi.ontopia.net/jsp/taglib/webed" prefix="webed" %>
<%@ taglib uri="http://psi.ontopia.net/jsp/taglib/tolog" prefix="tolog" %>

<tolog:context topicmap="test.ltm">
<webed:form actiongroup="testActionGroup" readonly="true">
<webed:invoke action="invokeTest"/>
</webed:form>
</tolog:context>
