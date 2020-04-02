package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.program.Program;

public interface FeatureFinder {

    boolean featureFound(Program oldVersion, Program curVersion);
}
