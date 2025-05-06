extends Node2D

@onready var character_list = $ScrollContainer/CharacterList
@onready var confirm_button = $ButtonConfirm
@onready var status_label = $StatusLabel

var selected_character: Character = null
var players := {}  # peer_id -> { character_name }
var local_id := 0

func _ready():
	local_id = multiplayer.get_unique_id()
	multiplayer.peer_disconnected.connect(_on_peer_disconnected)
	multiplayer.connected_to_server.connect(_on_connected)
	confirm_button.pressed.connect(_on_confirm_pressed)

	if multiplayer.is_server():
		status_label.text = "🧠 Eres el host. Esperando selección..."
	else:
		status_label.text = "👤 Conectado. Selecciona tu personaje."
		
func _on_peer_disconnected(peer_id: int):
	print("Jugador desconectado: ", peer_id)
	players.erase(peer_id)
	_update_status()

func _on_character_chosen(character: Character):
	selected_character = character
	status_label.text = "✅ Seleccionaste: " + character.name

func _on_confirm_pressed():
	if selected_character == null:
		status_label.text = "⚠️ Selecciona un personaje antes de continuar."
		return

	confirm_button.disabled = true
	status_label.text = "📨 Enviando selección..."
	
	if multiplayer.is_server():
		_register_character(local_id, selected_character.name)
	else:
		rpc_id(1, "send_character", local_id, selected_character.name)

@rpc("any_peer")
func send_character(peer_id: int, character_name: String):
	print("Jugador %d ha elegido %s" % [peer_id, character_name])
	_register_character(peer_id, character_name)

func _register_character(peer_id: int, character_name: String):
	players[peer_id] = { "character": character_name }
	_update_status()

	if multiplayer.is_server() and players.size() == 2:
		var all_selected = true
		for p in players.values():
			if not p.has("character"):
				all_selected = false
		if all_selected:
			print("✅ Ambos jugadores han elegido. Iniciando partida.")
			rpc("start_match")

func _update_status():
	var text = ""
	for id in players.keys():
		text += "Jugador %d: %s\n" % [id, players[id]["character"]]
	status_label.text = text

@rpc("any_peer")
func start_match():
	print("🚀 Cambiando a OnlineJCJScene...")
	get_tree().change_scene_to_file("res://Myassets/Scenes/OnlineJCJScene.tscn")

func _on_connected():
	print("🔌 Cliente conectado al servidor.")
