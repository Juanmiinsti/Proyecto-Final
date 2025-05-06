extends Control

@onready var player1_status = $VBoxContainer/Player1Status
@onready var player2_status = $VBoxContainer/Player2Status
@onready var ready_button = $ReadyButton
@onready var status_label = $StatusLabel

var players := {}
var is_connected = false

func _ready():
	multiplayer.connected_to_server.connect(_on_connected)
	multiplayer.peer_disconnected.connect(_on_peer_disconnected)
	ready_button.pressed.connect(_on_ready_pressed)

	if multiplayer.is_server():
		print("Servidor listo")
		is_connected = true  # SOLUCIÓN 1: marcar conectado si somos host
		var my_id = multiplayer.get_unique_id()
		players[my_id] = {"name": PlayerInfo.userName, "ready": false}
		_update_status_display()

func _on_connected():
	status_label.text = "🔗 Conectado al servidor"
	is_connected = true
	print("Cliente conectado al servidor")
	if !multiplayer.is_server():
		print("Registrando jugador desde cliente...")
		rpc_id(1, "register_player", multiplayer.get_unique_id(), PlayerInfo.userName)

@rpc("any_peer")
func register_player(peer_id: int, name: String):
	print("Registrando jugador:", name, "ID:", peer_id)
	players[peer_id] = {"name": name, "ready": false}
	_update_status_display()

	if multiplayer.is_server():
		for id in players:
			rpc_id(peer_id, "register_player", id, players[id]["name"])
			if players[id]["ready"]:
				rpc_id(peer_id, "mark_ready", id)

@rpc("any_peer")
func mark_ready(peer_id: int):
	print("Recibido 'listo' de ID:", peer_id)

	if players.has(peer_id):
		players[peer_id]["ready"] = true
		_update_status_display()

		if multiplayer.is_server():
			rpc_id(peer_id, "mark_ready", peer_id)  # Reflejar visualmente en cliente

			if players.size() == 2 and players.values().all(func(p): return p.ready):
				print("¡Todos listos! Iniciando partida...")
				start_match()  # 🚀 El servidor también cambia de escena
				rpc("start_match")  # Y lo envía a los clientes


func _on_ready_pressed():
	if is_connected:
		print("Botón 'Listo' presionado")
		var my_id = multiplayer.get_unique_id()
		if multiplayer.is_server():
			mark_ready(my_id)
		else:
			print("Cliente: enviando RPC al servidor")
			rpc_id(1, "mark_ready", my_id)

		ready_button.disabled = true
		status_label.text = "⏳ Esperando al otro jugador..."
	else:
		status_label.text = "❌ No estás conectado al servidor."
		print("No conectado al servidor")

@rpc("any_peer")
func start_match():
	print("start_match recibido")
	status_label.text = "🚀 Ambos listos, entrando en combate..."
	await get_tree().create_timer(1.0).timeout
	SceneManager.go_to("res://Myassets/Scenes/characterSelectionOnline.tscn")

func _update_status_display():
	var player_ids = players.keys()
	var name_1 = "Esperando..."
	var name_2 = "Esperando..."
	var ready_1 = ""
	var ready_2 = ""

	if player_ids.size() >= 1 and players.has(player_ids[0]):
		var p1 = players[player_ids[0]]
		name_1 = p1.get("name", "Jugador 1")
		ready_1 = "🟢 Listo" if p1.get("ready", false) else "🕐 No listo"

	if player_ids.size() >= 2 and players.has(player_ids[1]):
		var p2 = players[player_ids[1]]
		name_2 = p2.get("name", "Jugador 2")
		ready_2 = "🟢 Listo" if p2.get("ready", false) else "🕐 No listo"

	player1_status.text = name_1 + " - " + ready_1
	player2_status.text = name_2 + " - " + ready_2

func _on_peer_disconnected(peer_id: int):
	print("Jugador desconectado:", peer_id)
	if players.has(peer_id):
		players.erase(peer_id)
		_update_status_display()
		status_label.text = "❌ Jugador desconectado"
