package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.ProgramCode;

import java.text.ParseException;

public interface CodeParser {

    ProgramCode parse(String code) throws ParseException;
}
