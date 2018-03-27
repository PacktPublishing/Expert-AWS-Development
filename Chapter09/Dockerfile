FROM ubuntu:12.04

# Update the docker and Install dependency like apache
RUN apt-get update -y
RUN apt-get install -y apache2

# Create web page with Hi from Packt message
RUN echo "Hi from Packt!!! This is CI CD pipeline using Amazon ECS" > /var/www/index.html
	
# Configure the apache
RUN a2enmod rewrite
RUN chown -R www-data:www-data /var/www
ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2
	
# Mention the port number
EXPOSE 80

# Command to start apache web server
CMD ["/usr/sbin/apache2", "-D",  "FOREGROUND"]