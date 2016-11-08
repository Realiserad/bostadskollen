<!doctype html>
<html style="-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;overflow-x: hidden;font-family: Verdana,sans-serif;font-size: 15px;line-height: 1.5;">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
  </head>
  <body style="padding: 15px;margin: 0;font-family: Verdana,sans-serif;font-size: 15px;line-height: 1.5;">
    <div style="max-width:500px;">
      <div style="border-style: solid;border-width:1px;border-color:#2196F3;">
	    <header style="display: block;padding: 0.01em 16px;color: #fff!important;background-color: #2196F3!important;">
		  <h3 style="font-family: &quot;Segoe UI&quot;,Arial,sans-serif;font-size: 24px;font-weight: 400;margin: 10px 0;">${title}</h3>
        </header>
	    <div style="padding: 0.01em 16px;">
          <p>Hej!</p>
          <p>${text}</p>
	      <ul>
	        <#list properties as property>
              <li><b>${property.getKey()}:</b> ${property.getValue()}</li>
            </#list>
	      </ul>
	    </div>
	    <a href="${link}" style="background-color: transparent;color: inherit;">
	      <button style="font: inherit;margin: 0;overflow: hidden;text-transform: none;-webkit-appearance: button;cursor: pointer;border: none;display: inline-block;outline: 0;padding: 16px 16px;vertical-align: middle;color: #fff!important;background-color: #2196F3!important;text-align: center;white-space: nowrap;width: 100%;-webkit-transition: background-color .3s,color .15s,box-shadow .3s,opacity 0.3s;transition: background-color .3s,color .15s,box-shadow .3s,opacity 0.3s;text-decoration: none!important;">Jag vill veta mer!</button> 
	    </a>
	  </div>
	  <div align="center">
	    <p>
	      <small style="font-size: 80%;">Utskickad av Bostadskollen.</small>
		  <br>
		  <small style="font-size: 80%;">Vill du avsluta din prenumeration? <a href="${unsubscribe}" style="background-color: transparent;color: inherit;">Avregistrera dig</a>.</small>
	    </p>
	  </div> 
	</div>
  </body>
</html>