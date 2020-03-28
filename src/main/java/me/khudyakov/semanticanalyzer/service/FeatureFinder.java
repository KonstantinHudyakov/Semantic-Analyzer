package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.program.Program;

public interface FeatureFinder {

    boolean featureFound(Program oldVersion, Program curVersion);
}
