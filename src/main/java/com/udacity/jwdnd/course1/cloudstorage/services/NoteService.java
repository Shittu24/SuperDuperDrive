package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public List<Note> getNotes(Integer userId) {
        return noteMapper.findByUserId(userId);
    }

    public void addNote(Note note) {
        noteMapper.insert(note);
    }

    public void updateNote(Note note) {
        noteMapper.update(note);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
    }
}
