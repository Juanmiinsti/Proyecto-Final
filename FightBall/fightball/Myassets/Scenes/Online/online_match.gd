extends Node2D

var mode_selector_scene := preload("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")

# Referencias a los jugadores
var playersJoined :int=0

func _ready():
	set_meta("scene_path", "res://Myassets/Scenes/OnlineMatch/OnlineMatch.tscn")
	add_player(multiplayer.get_unique_id())
	if multiplayer.is_server():
		# Solo el servidor inicia el combate y asigna datos
		setup_players()
		CurrentMatch.start_timer()

	update_victory_indicators()

func add_player(id:int):
	print("add player:")
	print(id)
	var character = preload("res://Myassets/Entities/PCharacter/PcharacterOnline.tscn").instantiate()
	var pos=Vector2(94,-90)
	if playersJoined==0:
		pos=Vector2(-33,55)
	else:
		pos=Vector2(304,-55)
	character.position =pos
	
	playersJoined+=playersJoined+1
	$Players.add_child(character,true)
		
func setup_players():
	# Carga los datos de los personajes para ambos jugadores
	var characterp1 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname1)) - 1]

	

func setup_player(player: pCharacter, character_data, player_num: int):
	if multiplayer.is_server():
		player.numPlayer = 1
	else:
		player.numPlayer = 2	
	player.character = character_data
	player.HealthBar.max_value = character_data.max_health
	player.currentHealth = character_data.max_health
	player.HealthBar.value = character_data.max_health
	player.damage = character_data.damage

func update_victory_indicators():
	if CurrentMatch.p1Victory >= 1:
		$win1P1.color = Color.GREEN
	if CurrentMatch.p1Victory >= 2:
		$win2P1.color = Color.GREEN
	if CurrentMatch.p2Victory >= 1:
		$win1P2.color = Color.GREEN
	if CurrentMatch.p2Victory >= 2:
		$win2P2.color = Color.GREEN


# 🛠️ Función remota para aplicar daño
@rpc("any_peer")
func request_damage(target_id: int, amount: int):
	if not multiplayer.is_server():
		return
	
	#var target = (target_id == 1) if p1 else p2
	#target.take_damage(amount)
	#check_victory()
#
## ✔️ Verifica si alguien ganó la partida
#func check_victory():
	#if p1.currentHealth <= 0:
		#CurrentMatch.p2Victory += 1
	#elif p2.currentHealth <= 0:
		#CurrentMatch.p1Victory += 1
	#else:
		#return
	
	if CurrentMatch.p1Victory >= 2 or CurrentMatch.p2Victory >= 2:
		# Lógica de victoria final
		process_match_end()
	else:
		# Reiniciar escena entre rondas
		CurrentMatch.firstTime = false
		get_tree().reload_current_scene()

func process_match_end():
	# Determina ganador y perdedor
	if CurrentMatch.p1Victory >= 2:
		CurrentMatch.char_winner_id = getcharid(Persistence.charname1)
		CurrentMatch.char_loser_id = getcharid(Persistence.charname2)
		CurrentMatch.user_winner_id = PlayerInfo.userID
		CurrentMatch.user_loser_id = -1
	else:
		CurrentMatch.char_winner_id = getcharid(Persistence.charname2)
		CurrentMatch.char_loser_id = getcharid(Persistence.charname1)
		CurrentMatch.user_winner_id = -1
		CurrentMatch.user_loser_id = PlayerInfo.userID

	# Fecha
	CurrentMatch.date = Time.get_datetime_string_from_system()
	
	# Enviar resultados
	CurrentMatch.sendMatch()

	# Reiniciar contadores
	CurrentMatch.p1Victory = 0
	CurrentMatch.p2Victory = 0

	# Volver a selección de modo
	SceneManager.go_to(mode_selector_scene.resource_path, false)


func getcharid(name: String) -> int:
	if name == "Huntress":
		return 1
	elif name == "Martial":
		return 2
	else:
		return 3
