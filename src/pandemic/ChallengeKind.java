package pandemic;

import java.io.Serializable;

public enum ChallengeKind implements Serializable{
  OriginalBaseGame, OnTheBrinkNoChallenges, VirulentStrain, BioTerrorist, Mutation,
  VirulentStrainAndMutation, VirulentStrainAndBioTerrorist
}
