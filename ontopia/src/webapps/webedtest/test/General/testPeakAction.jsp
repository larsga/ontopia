<%@ taglib uri="/WEB-INF/jsp/webed-form.tld" prefix="webed" %>
<%@ taglib uri="/WEB-INF/jsp/tolog.tld" prefix="tolog"     %>

<tolog:context topicmap="test.ltm">
<webed:form actiongroup="testActionGroup">
<webed:button action="peakAction" id="BTN" text="Peak"/>
<webed:field type="textField" action="dummy" id="FLD">VALUE</webed:field>
</webed:form>
</tolog:context>