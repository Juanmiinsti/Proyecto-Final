extends Node2D
class_name JcJ

## Manages the Player-vs-Player (JcJ) match scene.
##
## This node is responsible for initializing the characters' stats,
## displaying victory indicators, and handling match progression including
## determining winners, updating match data, and transitioning between rounds or scenes.
##
## Character stats are loaded either from default values or from the database via
## `CharacterDataSource.DBcharacters`. Victory states are tracked using `CurrentMatch`.
##
## Scene transitions and match registration are performed using `SceneManager` and `CurrentMatch.sendMatch()`.
##
## @see [method setDefaultStats] for assigning default character values.
## @see [method getcharid] for resolving character IDs from names.
## @see [method _on_progress_bar_value_changed] for handling Player 2 defeat.
## @see [method _on_progress_bar_2_value_changed] for handling Player 1 defeat.

# Preload the mode selector scene to allow returning after the match
var mode_selector_scene := preload("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")

## Called when the node enters the scene tree.
## Initializes UI indicators, character stats, and match timer.
func _ready() -> void:
	set_meta("scene_path", "res://Myassets/Scenes/JCJScene/JCJScene.tscn")
	CurrentMatch.start_timer()

	# Update visual victory indicators for Player 1
	if CurrentMatch.p1Victory >= 1:
		$win1P1.color = Color.GREEN
	if CurrentMatch.p1Victory >= 2:
		$win2P1.color = Color.GREEN

	# Update visual victory indicators for Player 2
	if CurrentMatch.p2Victory >= 1:
		$win1P2.color = Color.GREEN
	if CurrentMatch.p2Victory >= 2:
		$win2P2.color = Color.GREEN

	# Initialize Player 1
	var p1 = $player1
	if CharacterDataSource.DBcharacters.size() == 0:
		setDefaultStats(p1)
	else:
		var characterp1 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname1)) - 1]
		p1.HealthBar.max_value = characterp1.max_health
		p1.currentHealth = characterp1.max_health
		p1.HealthBar.value = characterp1.max_health
		p1.damage = characterp1.damage

	# Initialize Player 2
	var p2 = $player2
	if CharacterDataSource.DBcharacters.size() == 0:
		setDefaultStats(p2)
	else:
		var characterp2 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname2)) - 1]
		p2.HealthBar.max_value = characterp2.max_health
		p2.currentHealth = characterp2.max_health
		p2.HealthBar.value = characterp2.max_health
		p2.damage = characterp2.damage

func _process(delta: float) -> void:
	pass

## Assigns default stats to a player character in case DB data is unavailable.
##
## @param char Character node to assign default stats to.
func setDefaultStats(char: pCharacter) -> void:
	char.HealthBar.max_value = 100
	char.currentHealth = 100
	char.HealthBar.value = 100
	char.damage = 10

## Returns the character ID based on the name string.
##
## @param name Character name.
## @return Integer ID of the character, or 3 if unrecognized.
func getcharid(name: String) -> int:
	if name == 'Huntress':
		return 1
	elif name == 'Martial':
		return 2
	else:
		return 3

## Handles Player 2 defeat logic.
##
## Triggered when Player 2’s health bar reaches zero.
## Updates victory count, checks for match end, and either reloads the scene or ends the match.
##
## @param value Current value of Player 2’s health bar.
func _on_progress_bar_value_changed(value: float) -> void:
	if not CurrentMatch.firstTime and value <= 0:
		CurrentMatch.p2Victory += 1

		if CurrentMatch.p2Victory >= 2:
			# Player 2 wins the match
			CurrentMatch.char_loser_id = getcharid(Persistence.charname1)
			CurrentMatch.char_winner_id = getcharid(Persistence.charname2)
			CurrentMatch.user_winner_id = -1
			CurrentMatch.user_loser_id = PlayerInfo.userID

			CurrentMatch.date = Time.get_datetime_string_from_system()
			CurrentMatch.sendMatch()

			# Reset match and return to mode selector
			CurrentMatch.p1Victory = 0
			CurrentMatch.p2Victory = 0
			SceneManager.go_to(mode_selector_scene.resource_path, false)
			Persistence.character = null
			Persistence.character2 = null
		else:
			get_tree().reload_current_scene()

	if CurrentMatch.firstTime:
		CurrentMatch.firstTime = false

## Handles Player 1 defeat logic.
##
## Triggered when Player 1’s health bar reaches zero.
## Updates victory count, checks for match end, and either reloads the scene or ends the match.
##
## @param value Current value of Player 1’s health bar.
func _on_progress_bar_2_value_changed(value: float) -> void:
	if not CurrentMatch.firstTime and value <= 0:
		CurrentMatch.p1Victory += 1

		if CurrentMatch.p1Victory >= 2:
			# Player 1 wins the match
			CurrentMatch.char_loser_id = getcharid(Persistence.charname2)
			CurrentMatch.char_winner_id = getcharid(Persistence.charname1)
			CurrentMatch.user_winner_id = PlayerInfo.userID
			CurrentMatch.user_loser_id = -1

			CurrentMatch.date = Time.get_datetime_string_from_system()
			CurrentMatch.sendMatch()

			# Reset match and return to mode selector
			CurrentMatch.p1Victory = 0
			CurrentMatch.p2Victory = 0
			SceneManager.go_to(mode_selector_scene.resource_path, false)
			Persistence.character = null
			Persistence.character2 = null
		else:
			get_tree().reload_current_scene()

	if CurrentMatch.firstTime:
		CurrentMatch.firstTime = false
