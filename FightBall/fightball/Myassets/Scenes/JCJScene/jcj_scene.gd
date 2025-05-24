extends Node2D

# Preload the mode selector scene for later navigation
var mode_selector_scene := preload("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")

## Called when the node enters the scene tree for the first time.
## Sets up the initial UI state and character stats based on current match data.
func _ready() -> void:
	# Set scene path metadata for SceneManager navigation
	set_meta("scene_path", "res://Myassets/Scenes/JCJScene/JCJScene.tscn")
	
	# Start the match timer (assumed external singleton)
	CurrentMatch.start_timer()

	# Update victory indicators for Player 1 (green if player has >= 1 or 2 wins)
	if CurrentMatch.p1Victory >= 1:
		$win1P1.color = Color.GREEN
		print('cambiandop1 1')
	if CurrentMatch.p1Victory >= 2:
		$win2P1.color = Color.GREEN
		print('cambiandop1 2')

	# Update victory indicators for Player 2 (green if player has >= 1 or 2 wins)
	if CurrentMatch.p2Victory >= 1:
		$win1P2.color = Color.GREEN
		print('cambiandop2 1')
	if CurrentMatch.p2Victory >= 2:
		$win2P2.color = Color.GREEN
		print('cambiandop2 2')

	# Variables to hold character data
	var characterp1
	var characterp2

	var p1 = $player1
	# Setup Player 1 stats: either default or from DB character data
	if CharacterDataSource.DBcharacters.size() == 0:
		setDefaultStats(p1)
	else:
		characterp1 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname1)) - 1]
		print(characterp1.max_health)
		p1.HealthBar.max_value = characterp1.max_health
		p1.currentHealth = characterp1.max_health
		p1.HealthBar.value = characterp1.max_health
		p1.damage = characterp1.damage

	var p2 = $player2
	# Setup Player 2 stats: either default or from DB character data
	if CharacterDataSource.DBcharacters.size() == 0:
		setDefaultStats(p2)
	else:
		characterp2 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname2)) - 1]
		print(characterp2.max_health)
		p2.HealthBar.max_value = characterp2.max_health
		p2.currentHealth = characterp2.max_health
		p2.HealthBar.value = characterp2.max_health
		p2.damage = characterp2.damage

## Called every frame. 'delta' is elapsed time since previous frame.
func _process(delta: float) -> void:
	pass

## Sets default stats on a given character if no DB data is available.
func setDefaultStats(char: pCharacter):
	char.HealthBar.max_value = 100
	char.currentHealth = 100
	char.HealthBar.value = 100
	char.damage = 10

## Returns the character ID based on the character's name.
## If name is not recognized, returns 3 as default.
func getcharid(name: String):
	if name == 'Huntress':
		return 1
	elif name == 'Martial':
		return 2
	else:
		return 3    

## Called when Player 2's health bar value changes.
## Handles logic for when Player 2 receives damage or is defeated.
func _on_progress_bar_value_changed(value: float) -> void:
	# If not the first match and Player 2's health drops to zero or less
	if not CurrentMatch.firstTime and value <= 0:
		CurrentMatch.p2Victory += 1
		print("Victories P2:", CurrentMatch.p2Victory)

		# If Player 2 reaches 2 victories, end match and record results
		if CurrentMatch.p2Victory >= 2:
			var p1 = $player1
			var p2 = $player2
			print(p1.name)
			print(getcharid(p1.name))

			print("🏁 current health value:", value)
			print("🏁 firstTime:", CurrentMatch.firstTime)
			print("🏁 p1Victory:", CurrentMatch.p1Victory)
			print("🏁 p2Victory:", CurrentMatch.p2Victory)
			print("🏁 Calling SceneManager:", SceneManager)

			# Reset victory counters for next match
			CurrentMatch.p2Victory = 0
			CurrentMatch.p1Victory = 0

			# Record winner and loser character IDs
			CurrentMatch.char_loser_id = getcharid(Persistence.charname1)
			CurrentMatch.char_winner_id = getcharid(Persistence.charname2)

			# Set winner and loser user IDs (assuming PlayerInfo.userID is local player)
			if CurrentMatch.char_winner_id == getcharid(Persistence.charname1):
				CurrentMatch.user_winner_id = PlayerInfo.userID
				CurrentMatch.user_loser_id = -1  # Player 2 is AI/local, no user ID
			else:
				CurrentMatch.user_winner_id = -1
				CurrentMatch.user_loser_id = PlayerInfo.userID			

			# Register current datetime for the match
			CurrentMatch.date = Time.get_datetime_string_from_system()

			# Send match data to backend or processing system
			CurrentMatch.sendMatch()

			# Navigate back to mode selector scene
			SceneManager.go_to("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn", false)
			Persistence.character = null
			Persistence.character2 = null

		# If Player 2 lost but hasn't reached 2 victories yet, reload the scene for a rematch
		elif not CurrentMatch.firstTime:
			get_tree().reload_current_scene()

	# After first damage event, mark firstTime as false
	if CurrentMatch.firstTime:
		CurrentMatch.firstTime = false

## Called when Player 1's health bar value changes.
## Handles logic for when Player 1 receives damage or is defeated.
func _on_progress_bar_2_value_changed(value: float) -> void:
	# If not the first match and Player 1's health drops to zero or less
	if not CurrentMatch.firstTime and value <= 0:
		CurrentMatch.p1Victory += 1
		print("Victories P1:", CurrentMatch.p1Victory)

		# If Player 1 reaches 2 victories, end match and record results
		if CurrentMatch.p1Victory >= 2:
			var p1 = $player1
			var p2 = $player2

			print(p1.name)
			print(getcharid(p1.name))

			# Record winner and loser character IDs
			CurrentMatch.char_loser_id = getcharid(Persistence.charname2)
			CurrentMatch.char_winner_id = getcharid(Persistence.charname1)

			# Set winner and loser user IDs (assuming PlayerInfo.userID is local player)
			if CurrentMatch.char_winner_id == getcharid(Persistence.charname1):
				CurrentMatch.user_winner_id = PlayerInfo.userID
				CurrentMatch.user_loser_id = -1  # Player 2 is AI/local, no user ID
			else:
				CurrentMatch.user_winner_id = -1
				CurrentMatch.user_loser_id = PlayerInfo.userID

			# Reset victory counters for next match
			CurrentMatch.p2Victory = 0
			CurrentMatch.p1Victory = 0

			# Register current datetime for the match
			CurrentMatch.date = Time.get_datetime_string_from_system()

			# Send match data to backend or processing system
			CurrentMatch.sendMatch()

			# Navigate back to mode selector scene
			SceneManager.go_to("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn", false)
			Persistence.character = null
			Persistence.character2 = null

		# If Player 1 lost but hasn't reached 2 victories yet, reload the scene for a rematch
		elif not CurrentMatch.firstTime:
			get_tree().reload_current_scene()

	# After first damage event, mark firstTime as false
	if CurrentMatch.firstTime:
		CurrentMatch.firstTime = false
