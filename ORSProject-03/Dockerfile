
FROM tomcat:9
COPY target/ORSProject-03.war /usr/local/tomcat/webapps/ORSProject-03.war
COPY Report/Report.jrxml /opt/reports/Report.jrxml
EXPOSE 8080
CMD ["catalina.sh", "run"]