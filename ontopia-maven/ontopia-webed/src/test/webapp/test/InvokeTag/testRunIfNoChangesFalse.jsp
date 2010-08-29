<%@ taglib uri="http://psi.ontopia.net/jsp/taglib/webed" prefix="webed" %>
<%@ taglib uri="http://psi.ontopia.net/jsp/taglib/tolog" prefix="tolog" %>

<tolog:context topicmap="test.ltm">
<webed:form actiongroup="testActionGroup">
<webed:button action="dummy" id="BTN" text="BUTTON"/>
<webed:invoke action="testAction" runIfNoChanges="false"/>
<webed:field type="textField" action="testValueAction" id="FLD" >VALUE</webed:field>
</webed:form>
</tolog:context>
