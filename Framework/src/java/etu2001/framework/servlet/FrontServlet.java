/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu2018.framework.servlet;

import etu2018.framework.*;
import etu2018.framework.annotation.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.lang.reflect.*;
import java.rmi.ServerException;



public class FrontServlet extends HttpServlet {
    HashMap<String,Mapping> MappingUrls;
    public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }
        return classes;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h3>Servlet UrlController at " + request.getServletPath() + "</h3>");
        out.println("<p>" + request.getContextPath() + "</p>");
        File f = new File("C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/framework/WEB-INF/classes/model");
        /*File[] files = f.listFiles();
        for (File file : files){
            if(file.getName().endsWith(".class")){
                out.println(file.getName());
            }  
        }*/

        try{
            List<Class<?>> classes = FrontServlet.findClasses(f,"model");
            for(int i = 0;i<classes.size();i++){
                Class<?> clazz = classes.get(i);
                Method[] methods = clazz.getDeclaredMethods();

                for (Method method : methods) {
                    if (method.isAnnotationPresent(etu2018.framework.annotation.Annotation.class)) {
                        Annotation annotation = method.getAnnotation(etu2018.framework.annotation.Annotation.class);
                        String url = annotation.url();
                        if (request.getServletPath().equals(url)) {
                            // Do something with the method
                            out.println("io le izy eh : " + method.getName() + "() waaaaaah!!");
                        }
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
            
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
