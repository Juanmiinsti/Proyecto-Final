extends Node2D


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	if CurrentMatch.p1Victory >= 1:
		$win1P1.color = Color.GREEN
		print('cambiandop1 1')
	if CurrentMatch.p1Victory >= 2:
		$win2P1.color = Color.GREEN
		print('cambiandop1 2')	
		
	if CurrentMatch.p2Victory >= 1:
		print('cambiandop2 1')
		$win1P2.color = Color.GREEN
	if CurrentMatch.p2Victory >= 2:
		print('cambiandop2 2')
		$win2P2.color = Color.GREEN	
	
	var characterp1
	var characterp2
	var p1=$player1
	
	if CharacterDataSource.DBcharacters.size()==0:
		setDefaultStats(p1)
	else:
		characterp1=CharacterDataSource.DBcharacters[getcharid(p1.name)]
		print(characterp1.max_health)
		p1.HealthBar.max_value=characterp1.max_health
		p1.currentHealth=characterp1.max_health
		p1.HealthBar.value=characterp1.max_health
		p1.damage=characterp1.damage
	
	var p2=$player2
	
	if CharacterDataSource.DBcharacters.size()==0:
		setDefaultStats(p2)
	else:
		characterp2=CharacterDataSource.DBcharacters[getcharid(p2.name)]
		print(characterp2.max_health)
		p2.HealthBar.max_value=characterp2.max_health
		p2.currentHealth=characterp2.max_health
		p2.HealthBar.value=characterp2.max_health
		p2.damage=characterp2.damage

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass

func setDefaultStats(char:pCharacter):
	char.HealthBar.max_value=100
	char.currentHealth=100
	char.HealthBar.value=100
	char.damage=10
	

func getcharid (name:String):
	if name=='martial':
		return 0
	elif name=='huntress':
		return 1
	else:
		return 2	


func _on_progress_bar_value_changed(value: float) -> void:
	if  not CurrentMatch.firstTime and value<=0:
		CurrentMatch.p2Victory += 1
		
		print("Victorias P2:", CurrentMatch.p2Victory)	
		if CurrentMatch.p2Victory >= 2:
			var p1=$player1
			var p2=$player2
			print(p1.name)
			print(getcharid(p1.name))
			get_tree().change_scene_to_file("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")
			CurrentMatch.p2Victory=0
			CurrentMatch.p1Victory=0
			CurrentMatch.date= Time.get_datetime_string_from_system()
			CurrentMatch.sendMatch()
			
		elif  not CurrentMatch.firstTime:
			get_tree().reload_current_scene()
		
	if  CurrentMatch.firstTime:
		CurrentMatch.firstTime=false
	


func _on_progress_bar_2_value_changed(value: float) -> void:
	if  not CurrentMatch.firstTime and value<=0:
		CurrentMatch.p1Victory += 1
		
		print("Victorias P1:", CurrentMatch.p1Victory)	
		if CurrentMatch.p1Victory >= 2:
			var p1=$player1
			var p2=$player2
			
			get_tree().change_scene_to_file("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")
			print(p1.name)
			print(getcharid(p1.name))
			CurrentMatch.p2Victory=0
			CurrentMatch.p1Victory=0
			CurrentMatch.date= Time.get_datetime_string_from_system()
			CurrentMatch.sendMatch()
			
		elif  not CurrentMatch.firstTime:
			get_tree().reload_current_scene()
		
	if  CurrentMatch.firstTime:
		CurrentMatch.firstTime=false
	
