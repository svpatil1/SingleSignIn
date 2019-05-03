/**
 * 
 */
package com.dashboard.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dashboard.command.applicationCommand;
import com.dashboard.domain.Application;
import com.dashboard.service.ApplicationService;
import com.singlesignin.sqlscript.runSqlScript;

/**
 * @author Sanika Patil
 *
 */
@RestController
public class ApplicationController {
	@Autowired
	private ApplicationService applicationService;

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public ModelAndView about() {
		ModelAndView mav = new ModelAndView("/about");
		return mav;
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView upload(Model m) {
		m.addAttribute("command", new applicationCommand());
		ModelAndView mav = new ModelAndView("/upload");
		return mav;
	}
	
	/*
	 * uploadFiles() method is called when users(ISV and ADMIN) are uploading the file on server.
	 * It firstly stores the file temporarily.
	 */
	
	@RequestMapping(value = "/uploadSuccessful", method = RequestMethod.GET)
	public ModelAndView uploadSucess(Model m) {
		ModelAndView mav = new ModelAndView("/uploadSuccessful");
		return mav;
	}
	
	@RequestMapping(value = "/uploadFailed", method = RequestMethod.GET)
	public ModelAndView uploadFail(Model m) {
		ModelAndView mav = new ModelAndView("/uploadFailed");
		return mav;
	}
	
	@RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
	public ModelAndView uploadFiles(@ModelAttribute("command") applicationCommand cmd, Model m,  HttpSession session, @RequestParam("file") MultipartFile[] files) throws IOException, SQLException
	{	
		if(cmd.getAppDescription().isEmpty()) {
			m.addAttribute("err", "Application Description or Application name cannot be empty"); // it takes care that the app description is not empty
			ModelAndView mav = new ModelAndView("/upload");
			return mav;
			
		}
		else if(cmd.getName().isEmpty()) {
			m.addAttribute("err", "Application Description or Application name cannot be empty"); // it takes care that the app description is not empty
			ModelAndView mav = new ModelAndView("/upload");
			return mav;
			
		}
				
		else {
			if((files[0].getOriginalFilename().endsWith(".war")) && (files[1].getOriginalFilename().endsWith(".sql"))) {
					
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				System.out.println("Original file name: "+ file.getOriginalFilename());
				System.out.println(file);
				
		
				try {
					
					byte[] bytes = file.getBytes();
	
					// Creating the directory to store file
					String rootPath = System.getProperty("catalina.base");
					System.out.println(rootPath);
					
					File dirSql = new File(rootPath + File.separator + "tmpFiles");
					File dirWar = new File(rootPath + File.separator + "warFiles");
					
					System.out.println("Sql directory"+dirSql);
					System.out.println("War directory"+dirWar);
					
	
					// Create temporary sql file on server
					if((file.getOriginalFilename().endsWith(".sql"))){
						
						if (!dirSql.exists()) { // check if directory already exist
							dirSql.mkdirs();
							System.out.println("SQL Directory has been created successfully");
						}
						
						File TempServerFile = new File(dirSql.getAbsolutePath()
								+ File.separator + file.getOriginalFilename());
						System.out.println("SQL Temp server file: "+ TempServerFile );
						String scriptPathSql = TempServerFile.toString();
						
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(TempServerFile));
						bos.write(bytes);
						bos.close();
						
						/*
						 * If the file is an sql script then run and delete it from the directory to save memory.
						 */
						
						runSqlScript test = new runSqlScript();
						test.run(scriptPathSql);
						TempServerFile.delete();
					}
					else {

						//Registering a new application in database, after uploading
						Application a = new Application();
						try {
						a.setName(cmd.getName());
						a.setUserId((Integer) session.getAttribute("userId"));
						a.setAppDesc(cmd.getAppDescription());
						
						String iconName = cmd.getName() + ".png";
						String iconPath = "resources/img/" + iconName;
						a.setImageLocation(iconPath);
						System.out.println(a.toString());
						applicationService.registerApplication(a);
						}catch (Exception e){
							m.addAttribute("err", "The Application with same name already exist"); // it takes care that the app description is not empty
							ModelAndView mav = new ModelAndView("/upload");
							return mav;
						}
						
						if (!dirWar.exists()) { // check if directory already exist
							dirWar.mkdirs();
							System.out.println("War Directory has been created successfully");
						}
						
						File TempServerFileWar = new File(dirWar.getAbsolutePath()
								+ File.separator + file.getOriginalFilename());
						System.out.println("War Temp server file: "+ TempServerFileWar );						
						BufferedOutputStream bosWar = new BufferedOutputStream(new FileOutputStream(TempServerFileWar));
						bosWar.write(bytes);
						bosWar.close();
						
						
						
					}	
					}
				
				catch (Exception e) {
					ModelAndView mav = new ModelAndView("redirect:uploadFailed");
					return mav;
				}
			}
			
}else {
	m.addAttribute("err", "Select correct files.");
	ModelAndView mav = new ModelAndView("/upload");
	return mav;
		}
			ModelAndView mav = new ModelAndView("redirect:uploadSuccessful");
			return mav;
		
		}
	}
}
