extends Node2D

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	# Configuración inicial de las victorias mostradas en la UI
	
	# Si el jugador 1 tiene al menos 1 victoria, cambia el color del primer indicador a verde
	if CurrentMatch.p1Victory >= 1:
		$win1P1.color = Color.GREEN
		print('cambiandop1 1')
	# Si el jugador 1 tiene al menos 2 victorias, cambia el color del segundo indicador a verde    
	if CurrentMatch.p1Victory >= 2:
		$win2P1.color = Color.GREEN
		print('cambiandop1 2')    
		
	# Si el jugador 2 tiene al menos 1 victoria, cambia el color del primer indicador a verde
	if CurrentMatch.p2Victory >= 1:
		print('cambiandop2 1')
		$win1P2.color = Color.GREEN
	# Si el jugador 2 tiene al menos 2 victorias, cambia el color del segundo indicador a verde    
	if CurrentMatch.p2Victory >= 2:
		print('cambiandop2 2')
		$win2P2.color = Color.GREEN    
	
	# Variables para almacenar los datos de los personajes
	var characterp1
	var characterp2
	var p1 = $player1
	
	# Configuración de estadísticas del jugador 1
	if CharacterDataSource.DBcharacters.size() == 0:
		# Si no hay personajes en la DB, usa valores por defecto
		setDefaultStats(p1)
	else:
		# Obtiene los datos del personaje del jugador 1 desde la DB
		characterp1 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname1))-1]
		print(characterp1.max_health)
		# Configura la barra de vida y estadísticas según los datos del personaje
		p1.HealthBar.max_value = characterp1.max_health
		p1.currentHealth = characterp1.max_health
		p1.HealthBar.value = characterp1.max_health
		p1.damage = characterp1.damage
	
	# Configuración de estadísticas del jugador 2
	var p2 = $player2
	
	if CharacterDataSource.DBcharacters.size() == 0:
		# Si no hay personajes en la DB, usa valores por defecto
		setDefaultStats(p2)
	else:
		# Obtiene los datos del personaje del jugador 2 desde la DB
		characterp2 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname2))-1]
		print(characterp2.max_health)
		# Configura la barra de vida y estadísticas según los datos del personaje
		p2.HealthBar.max_value = characterp2.max_health
		p2.currentHealth = characterp2.max_health
		p2.HealthBar.value = characterp2.max_health
		p2.damage = characterp2.damage

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass

# Establece estadísticas por defecto para un personaje
func setDefaultStats(char: pCharacter):
	char.HealthBar.max_value = 100
	char.currentHealth = 100
	char.HealthBar.value = 100
	char.damage = 10
	
# Devuelve el ID del personaje basado en su nombre
func getcharid(name: String):
	if name == 'Huntress':
		return 1
	elif name == 'Martial':
		return 2
	else:
		return 3    

# Se ejecuta cuando cambia el valor de la barra de vida del jugador 2 (cuando recibe daño)
func _on_progress_bar_value_changed(value: float) -> void:
	# Verifica si la vida llegó a 0 y no es la primera partida
	if not CurrentMatch.firstTime and value <= 0:
		# Incrementa las victorias del jugador 2
		CurrentMatch.p2Victory += 1
		
		print("Victorias P2:", CurrentMatch.p2Victory)    
		
		# Si el jugador 2 alcanza 2 victorias
		if CurrentMatch.p2Victory >= 2:
			var p1 = $player1
			var p2 = $player2
			print(p1.name)
			print(getcharid(p1.name))
			
			# Cambia a la escena del selector de modo
			get_tree().change_scene_to_file("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")
			
			# Reinicia contadores de victoria
			CurrentMatch.p2Victory = 0
			CurrentMatch.p1Victory = 0
			
			# Registra los IDs de los personajes ganador y perdedor
			CurrentMatch.char_loser_id = (getcharid(Persistence.charname1))
			CurrentMatch.char_winner_id =(getcharid(Persistence.charname2))
			
			# Registra la fecha actual
			CurrentMatch.date = Time.get_datetime_string_from_system()
			
			# Envía los datos del match
			CurrentMatch.sendMatch()
			
		# Si no ha ganado 2 veces pero perdió, recarga la escena    
		elif not CurrentMatch.firstTime:
			get_tree().reload_current_scene()
		
	# Marca que ya no es la primera partida    
	if CurrentMatch.firstTime:
		CurrentMatch.firstTime = false

# Se ejecuta cuando cambia el valor de la barra de vida del jugador 1 (cuando recibe daño)
func _on_progress_bar_2_value_changed(value: float) -> void:
	# Verifica si la vida llegó a 0 y no es la primera partida
	if not CurrentMatch.firstTime and value <= 0:
		# Incrementa las victorias del jugador 1
		CurrentMatch.p1Victory += 1
		
		print("Victorias P1:", CurrentMatch.p1Victory)    
		
		# Si el jugador 1 alcanza 2 victorias
		if CurrentMatch.p1Victory >= 2:
			var p1 = $player1
			var p2 = $player2
			
			# Cambia a la escena del selector de modo
			get_tree().change_scene_to_file("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")
			
			print(p1.name)
			print(getcharid(p1.name))
			
			# Registra los IDs de los personajes ganador y perdedor
			CurrentMatch.char_loser_id = (getcharid(Persistence.charname2))
			CurrentMatch.char_winner_id = (getcharid(Persistence.charname1))
			
			

			# Reinicia contadores de victoria
			CurrentMatch.p2Victory = 0
			CurrentMatch.p1Victory = 0
			
			# Registra la fecha actual
			CurrentMatch.date = Time.get_datetime_string_from_system()
			
			# Envía los datos del match
			CurrentMatch.sendMatch()
			
		# Si no ha ganado 2 veces pero perdió, recarga la escena    
		elif not CurrentMatch.firstTime:
			get_tree().reload_current_scene()
		
	# Marca que ya no es la primera partida    
	if CurrentMatch.firstTime:
		CurrentMatch.firstTime = false
