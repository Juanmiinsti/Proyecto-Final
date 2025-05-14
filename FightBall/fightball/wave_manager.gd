extends Node2D

@export var enemy_scene: PackedScene
@export var spawn_points: Array[NodePath] = []
@export var player: Node2D

var current_wave := 1
var enemies_alive := 0



func _ready():
	start_wave(current_wave)

func start_wave(wave_number: int):
	$"../waveNumber".text="WAVE "+str(wave_number)
	var enemies_to_spawn = 2 + wave_number
	enemies_alive = enemies_to_spawn
	$"../enemies_alive".text="Enemmies alive" + str(enemies_alive)
	print("OLEADA ", wave_number)

	for i in enemies_to_spawn:
		spawn_enemy()
		if get_tree() != null:
			await get_tree().create_timer(2.0).timeout

func spawn_enemy():
	var enemy_instance = enemy_scene.instantiate()
	$"../enemies_alive".text="Enemmies alive" + str(enemies_alive)
	enemy_instance.targetTochase=$"../NavigationRegion2D/player1"
	var spawn_position = get_node(spawn_points.pick_random()).global_position
	add_child(enemy_instance)
	enemy_instance.global_position = spawn_position

	# Conectamos al jugador
	

	# Restar enemigos cuando mueren
	enemy_instance.connect("tree_exited", Callable(self, "_on_enemy_died"))

func _on_enemy_died():
	$"../enemies_alive".text="Enemmies alive" + str(enemies_alive)
	enemies_alive -= 1
	if enemies_alive <= 0:
		print("Todos los enemigos eliminados. Próxima oleada...")
		if get_tree() != null:
			await get_tree().create_timer(2.0).timeout
		current_wave += 1
		start_wave(current_wave)
