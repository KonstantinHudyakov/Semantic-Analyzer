package me.khudyakov.staticanalyzer.service;

public interface FeatureFinder {

    boolean featureFound(SyntaxTreeChangesCache syntaxTreeChangesCache);
}
