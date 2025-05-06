extends Control

# Nodos de la interfaz
@onready var player1_status = $VBoxContainer/Player1Status
@onready var player2_status = $VBoxContainer/Player2Status
@onready var ready_button = $ReadyButton
@onready var status_label = $StatusLabel

# Diccionario para almacenar información de los jugadores
var players := {}  # clave: peer_id, valor: {name, ready}
var is_connected = false  # Para verificar la conexión

func _ready():
	# Conexión de señales de red
	multiplayer.connected_to_server.connect(_on_connected)
	multiplayer.peer_disconnected.connect(_on_peer_disconnected)
	ready_button.pressed.connect(_on_ready_pressed)
	
	# Si el jugador es el host, lo registramos automáticamente
	if multiplayer.is_server():
		var my_id = multiplayer.get_unique_id()
		players[my_id] = {"name": PlayerInfo.userName, "ready": false}
		_update_status_display()
	elif multiplayer.is_client():
		# El cliente se registra cuando se conecta
		rpc_id(1, "register_player", multiplayer.get_unique_id(), PlayerInfo.userName)

# Callback para la conexión exitosa al servidor
func _on_connected():
	status_label.text = "🔗 Conectado al servidor"
	is_connected = true  # Marcamos que ya estamos conectados

	# Si estamos en el rol de cliente, notificamos al servidor
	if multiplayer.is_client():
		rpc_id(1, "register_player", multiplayer.get_unique_id(), PlayerInfo.userName)

# RPC para registrar un nuevo jugador (solo llamado en el host)
@rpc("any_peer")
func register_player(peer_id: int, name: String):
	players[peer_id] = {"name": name, "ready": false}
	_update_status_display()

	# Sincronización con el nuevo cliente
	if multiplayer.is_server():
		for id in players:
			rpc_id(peer_id, "register_player", id, players[id]["name"])
			if players[id]["ready"]:
				rpc_id(peer_id, "mark_ready", id)

# RPC para marcar jugador como listo
@rpc("any_peer")
func mark_ready(peer_id: int):
	if players.has(peer_id):
		players[peer_id]["ready"] = true
		_update_status_display()

		# Solo el host verifica si todos están listos
		if multiplayer.is_server():
			if players.size() == 2 and players.values().all(func(p): return p.ready):
				rpc("start_match")

# Función llamada al presionar el botón de listo
func _on_ready_pressed():
	# Aseguramos que solo intentemos marcar como listo si estamos conectados al servidor
	if is_connected:
		if multiplayer.is_server():
			mark_ready(multiplayer.get_unique_id())  # El host marca al jugador como listo
		else:
			rpc_id(1, "mark_ready", multiplayer.get_unique_id())  # El cliente le avisa al host que está listo

		ready_button.disabled = true
		status_label.text = "⏳ Esperando al otro jugador..."
	else:
		status_label.text = "❌ No estás conectado al servidor."

# RPC para iniciar la partida (llamado por el host)
@rpc("any_peer")
func start_match():
	if multiplayer.is_server():  # Solo el host inicia la partida
		status_label.text = "🚀 Ambos listos, entrando en combate..."
		await get_tree().create_timer(1.0).timeout
		SceneManager.go_to("res://Myassets/Scenes/characterSelectionOnline.tscn")

# Actualiza la interfaz con el estado de los jugadores
func _update_status_display():
	var player_ids = players.keys()
	var name_1 = "Esperando..."
	var name_2 = "Esperando..."
	var ready_1 = ""
	var ready_2 = ""
	
	# Jugador 1
	if player_ids.size() >= 1 and players.has(player_ids[0]):
		var p1 = players[player_ids[0]]
		name_1 = p1.get("name", "Jugador 1")
		ready_1 = "🟢 Listo" if p1.get("ready", false) else "🕐 No listo"
	
	# Jugador 2
	if player_ids.size() >= 2 and players.has(player_ids[1]):
		var p2 = players[player_ids[1]]
		name_2 = p2.get("name", "Jugador 2")
		ready_2 = "🟢 Listo" if p2.get("ready", false) else "🕐 No listo"
	
	# Actualización de labels
	player1_status.text = name_1 + " - " + ready_1
	player2_status.text = name_2 + " - " + ready_2

# Callback para desconexión de un jugador
func _on_peer_disconnected(peer_id: int):
	if players.has(peer_id):
		players.erase(peer_id)
		_update_status_display()
		status_label.text = "❌ Jugador desconectado"
