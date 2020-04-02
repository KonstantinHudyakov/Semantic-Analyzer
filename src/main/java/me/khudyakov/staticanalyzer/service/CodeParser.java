package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.program.ProgramCode;

import java.text.ParseException;

public interface CodeParser {

    ProgramCode parse(String code) throws ParseException;
}
