package com.udacity.jwdnd.course1.cloudstorage.contoller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String homeView(Authentication authentication, Model model) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);
        List<Credential> credentials = credentialService.getCredentials(userId);
        credentials.forEach(credential -> {
            String decryptedPassword = credentialService.decryptPassword(credential.getPassword(), credential.getKey());
            credential.setDecryptedPassword(decryptedPassword);
        });

        model.addAttribute("notes", noteService.getNotes(userId));
        model.addAttribute("credentials", credentials);
        model.addAttribute("files", fileService.getFiles(userId));

        return "home";
    }

    @PostMapping("/file")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        try {
            if (!fileService.isFileNameAvailable(fileUpload.getOriginalFilename(), userId)) {
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("errorMessage", "You cannot upload the same file multiple times.");
            } else {
                fileService.addFile(fileUpload, userId);
                redirectAttributes.addFlashAttribute("success", true);
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error uploading the file.");
        }

        return "redirect:/result?success=" + redirectAttributes.getFlashAttributes().get("success")
                + "&errorMessage=" + redirectAttributes.getFlashAttributes().get("errorMessage");
    }

    @GetMapping("/file/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId, Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        File file = fileService.getFile(fileId, userId);
        if (file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/file/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error deleting the file.");
        }

        return "redirect:/result?success=" + redirectAttributes.getFlashAttributes().get("success")
                + "&errorMessage=" + redirectAttributes.getFlashAttributes().get("errorMessage");
    }

    @PostMapping("/note")
    public String addNote(Authentication authentication, @ModelAttribute Note note, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);
        note.setUserId(userId);

        try {
            if (note.getNoteId() != null) {
                noteService.updateNote(note);
            } else {
                noteService.addNote(note);
            }
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error saving the note.");
        }

        return "redirect:/result?success=" + redirectAttributes.getFlashAttributes().get("success")
                + "&errorMessage=" + redirectAttributes.getFlashAttributes().get("errorMessage");
    }

    @GetMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error deleting the note.");
        }

        return "redirect:/result?success=" + redirectAttributes.getFlashAttributes().get("success")
                + "&errorMessage=" + redirectAttributes.getFlashAttributes().get("errorMessage");
    }

    @PostMapping("/credential")
    public String addCredential(Authentication authentication, @ModelAttribute Credential credential, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);
        credential.setUserId(userId);

        try {
            if (credential.getCredentialId() != null) {
                credentialService.updateCredential(credential);
            } else {
                credentialService.addCredential(credential);
            }
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error saving the credential.");
        }

        return "redirect:/result?success=" + redirectAttributes.getFlashAttributes().get("success")
                + "&errorMessage=" + redirectAttributes.getFlashAttributes().get("errorMessage");
    }

    @GetMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error deleting the credential.");
        }

        return "redirect:/result?success=" + redirectAttributes.getFlashAttributes().get("success")
                + "&errorMessage=" + redirectAttributes.getFlashAttributes().get("errorMessage");
    }

}
