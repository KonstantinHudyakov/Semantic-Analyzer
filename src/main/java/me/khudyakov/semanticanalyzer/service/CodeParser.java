package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.program.ProgramCode;

import java.text.ParseException;

public interface CodeParser {

    ProgramCode parse(String code) throws ParseException;
}
