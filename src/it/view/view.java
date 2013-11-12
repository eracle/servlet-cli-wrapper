package it.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;


/**
 * Servlet implementation view class
 */
@WebServlet("/view")
public class view extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String[] command;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public view() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig servletConfig) throws ServletException{
	    this.command = servletConfig.getInitParameter("cmd").split("\\s");
	  }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//TODO:
		//extend the servlet functionality to the get requests
		ServletOutputStream response_data = response.getOutputStream();
		response_data.write("Lets try with an http post".getBytes());
		response_data.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// String command = "ls -la";
		// String log_file="log_file";

		// configuring the ProcessBuilder with the command str
		ProcessBuilder builder = new ProcessBuilder(command);

		// configuring the process builder
		builder.redirectInput(Redirect.PIPE);
		builder.redirectOutput(Redirect.PIPE);
		
		//TODO:
		//implement a better error log system (log4j?)
		builder.redirectError(Redirect.INHERIT);
		//builder.redirectError(Redirect.PIPE);

		// starting the subprocess
		Process p = builder.start();

		// get the request and the response streams
		ServletInputStream request_data = request.getInputStream();
		ServletOutputStream response_data = response.getOutputStream();

		// copying the http request data to the subprocess input
		OutputStream sub_process_input = p.getOutputStream();
		IOUtils.copy(request_data, sub_process_input);
		sub_process_input.close();

		// copying the output of the subprocess in the http response
		InputStream sub_process_output = p.getInputStream();
		IOUtils.copy(sub_process_output, response_data);
		sub_process_output.close();
		
		//copying the error stream in the http response (to be changed)
		/*
		InputStream sub_process_err = p.getErrorStream();
		IOUtils.copy(sub_process_err, response_data);
		sub_process_err.close();
		*/
		
	}

}
