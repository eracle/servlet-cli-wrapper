package it.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.Charset;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Servlet implementation view class
 */
@WebServlet("/view")
public class view extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final static Logger log = Logger.getLogger(view.class.getName());

	private String[] command;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public view() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.command = servletConfig.getInitParameter("cmd").split("\\s");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// logging phase
		log.info("Serving Get request");

		// TODO:
		// extend the servlet functionality to the get requests
		ServletOutputStream response_data = response.getOutputStream();
		response_data.write("Lets try with an http post".getBytes());
		response_data.close();

		log.debug("Served Get request");
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		log.info("["+this.command[0]+"]Serving Post request");
		
		// configuring the ProcessBuilder with the command str
		ProcessBuilder builder = new ProcessBuilder(command);

		// configuring the process builder
		builder.redirectInput(Redirect.PIPE);
		builder.redirectOutput(Redirect.PIPE);

		// configuring process stderr to be redirected to the log system
		builder.redirectError(Redirect.PIPE);

		Process p = null;
		try {
			log.trace("Starting the sub process");
					
			p = builder.start();
		} catch (IOException e) {
			
			// building the command str for passing it to the logger
			StringBuilder c = new StringBuilder();
			for (int i = 0; i < command.length; i++) {
				c.append(command[i]);
				c.append("[SPACE]");
			}
			log.fatal("Failed the command execution:\nCommand:\n"+c.toString(), e);
			return;
		}

		log.trace("Process has been started");
		
		// get the request and the response streams
		ServletInputStream request_data = request.getInputStream();
		ServletOutputStream response_data = response.getOutputStream();
		
		log.trace("Copying the http request data to the subprocess input");
		
		// copying the http request data to the subprocess input
		OutputStream sub_process_input = p.getOutputStream();
		IOUtils.copy(request_data, sub_process_input);
		sub_process_input.close();

		log.trace("Copying the output of the subprocess in the http response");
		
		// copying the output of the subprocess in the http response
		InputStream sub_process_output = p.getInputStream();
		IOUtils.copy(sub_process_output, response_data);
		sub_process_output.close();
	
		
		log.trace("Copying the error stream of the process (if there exists) to the log system");
		
		// copying the error stream of the process (if there exists)
		// to the log system
		InputStream sub_process_err = p.getErrorStream();
		StringWriter log4j_writer = new StringWriter();
		IOUtils.copy(sub_process_err, log4j_writer, Charset.forName("UTF-8"));
		sub_process_err.close();

		String log4jstr = log4j_writer.toString();

		// "if there exists" clause
		if (log4jstr != null)
			if (!log4jstr.equals(""))
				log.fatal(log4jstr);

		log.debug("Served Post request");
	}
}
